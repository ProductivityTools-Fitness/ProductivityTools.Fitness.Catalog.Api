package top.productivitytools.fitness.catalog.api.entitles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise")
@Getter
@Setter
@NoArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, length = 150)
    private String externalId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "primary_body_part", length = 100)
    private String primaryBodyPart;

    @Column(name = "primary_equipment", length = 100)
    private String primaryEquipment;

    @Column(name = "primary_muscle", length = 100)
    private String primaryMuscle;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "body_parts", nullable = false, columnDefinition = "jsonb")
    private List<String> bodyParts = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "equipments", nullable = false, columnDefinition = "jsonb")
    private List<String> equipments = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_muscles", nullable = false, columnDefinition = "jsonb")
    private List<String> targetMuscles = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "secondary_muscles", nullable = false, columnDefinition = "jsonb")
    private List<String> secondaryMuscles = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "instructions", nullable = false, columnDefinition = "jsonb")
    private List<String> instructions = new ArrayList<>();

    @Column(name = "original_gif_url", columnDefinition = "text")
    private String originalGifUrl;

    @Column(name = "image_file_name", length = 255)
    private String imageFileName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @PrePersist
    void onPrePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}
