package top.productivitytools.fitness.catalog.api.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Signature;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class FirebaseAuthFilterTest {

    private static final String PROJECT_ID = "ptprojectsweb";
    private static final String KID = "test-kid-1";
    private static final int INTERNAL_PORT = 8095;

    private static KeyPair keyPair;
    private FirebaseAuthFilter filter;

    @BeforeAll
    static void initKeys() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        keyPair = gen.generateKeyPair();
    }

    @BeforeEach
    void setUp() {
        filter = new FirebaseAuthFilter(
                new ObjectMapper(),
                PROJECT_ID,
                INTERNAL_PORT,
                kid -> KID.equals(kid) ? keyPair.getPublic() : null);
    }

    @Test
    void publicPortWithoutTokenReturns401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/exercises/search");
        request.setLocalPort(8085);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> chainCalled.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertFalse(chainCalled.get());
    }

    @Test
    void publicPortWithAllowedAdminTokenPasses() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/exercises/search");
        request.setLocalPort(8085);
        request.addHeader("Authorization", "Bearer " + signToken("pwujczyk@gmail.com"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> chainCalled.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertTrue(chainCalled.get());
        assertEquals("pwujczyk@gmail.com", request.getAttribute("authenticatedEmail"));
    }

    @Test
    void publicPortWithUnauthorizedEmailReturns403() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/exercises/search");
        request.setLocalPort(8085);
        request.addHeader("Authorization", "Bearer " + signToken("someone@example.com"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> chainCalled.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(403, response.getStatus());
        assertFalse(chainCalled.get());
    }

    @Test
    void internalPortAllowsGetWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/exercises/search");
        request.setLocalPort(INTERNAL_PORT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> chainCalled.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertTrue(chainCalled.get());
    }

    @Test
    void internalPortStillRejectsWriteWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/exercises/add");
        request.setLocalPort(INTERNAL_PORT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> chainCalled.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertFalse(chainCalled.get());
    }

    @Test
    void debugEndpointBypassesAuthOnPublicPort() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/debug/date");
        request.setLocalPort(8085);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainCalled = new AtomicBoolean(false);
        FilterChain chain = (req, res) -> chainCalled.set(true);

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertTrue(chainCalled.get());
    }

    private static String signToken(String email) throws Exception {
        long now = Instant.now().getEpochSecond();
        String header = "{\"alg\":\"RS256\",\"kid\":\"" + KID + "\"}";
        String payload = String.format(
                "{\"iss\":\"https://securetoken.google.com/%s\",\"aud\":\"%s\",\"sub\":\"user-123\",\"email\":\"%s\",\"iat\":%d,\"exp\":%d}",
                PROJECT_ID, PROJECT_ID, email, now - 10, now + 3600);
        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signingInput = encodedHeader + "." + encodedPayload;

        PrivateKey privateKey = keyPair.getPrivate();
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(signingInput.getBytes(StandardCharsets.US_ASCII));
        String encodedSig = Base64.getUrlEncoder().withoutPadding().encodeToString(sig.sign());

        return signingInput + "." + encodedSig;
    }
}
