package top.productivitytools.fitness.catalog.api.entitles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Binary content of the animated GIF that illustrates an exercise.
 *
 * <p>Kept in a separate table from {@link Exercise} on purpose: a catalog listing pulls
 * roughly 300 KB per animation, so mixing the bytes into the metadata row would make
 * every "give me all exercises" query needlessly expensive.
 */
@Entity
@Table(name = "exercise_image")
@Getter
@Setter
@NoArgsConstructor
public class ExerciseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false, unique = true)
    private Exercise exercise;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType = "image/gif";

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    /**
     * Deliberately not annotated with {@code @Lob}: on PostgreSQL that would store the
     * bytes as a large object and keep only an OID here, while the schema declares BYTEA.
     * A plain byte[] maps straight onto BYTEA.
     */
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @PrePersist
    void onPrePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}
