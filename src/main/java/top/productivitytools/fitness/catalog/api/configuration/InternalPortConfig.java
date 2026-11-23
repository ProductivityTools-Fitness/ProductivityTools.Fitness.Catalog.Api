package top.productivitytools.fitness.catalog.api.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes a second HTTP connector on an internal-only port (default 8095) alongside the
 * public OAuth-protected port (8085).
 *
 * <p>In GCP, firewall rule {@code allow-catalog-internal-8095} restricts port 8095 to the
 * VPC subnet ({@code 10.10.0.0/24}); on Ubuntu on-prem, port 8095 is not opened in UFW.
 * {@link top.productivitytools.fitness.catalog.api.security.FirebaseAuthFilter} allows
 * read-only ({@code GET}/{@code HEAD}) calls arriving on this port without a Firebase JWT
 * so {@code Workouts.Api} can search and import exercises without shared secrets.
 */
@Configuration
public class InternalPortConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> internalPortCustomizer(
            @Value("${catalog.internal-port:8095}") int internalPort,
            @Value("${server.port:8085}") int publicPort) {
        return factory -> {
            if (internalPort > 0 && internalPort != publicPort) {
                Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
                connector.setPort(internalPort);
                factory.addAdditionalConnectors(connector);
            }
        };
    }
}
