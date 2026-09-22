package top.productivitytools.fitness.catalog.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> findByExerciseId(String exerciseId);

    boolean existsByExerciseId(String exerciseId);

    /**
     * Searches exercises by any combination of name fragment, body part and equipment.
     * Every criterion is optional - a {@code null} switches that condition off.
     *
     * <p>Body part and equipment are matched against the whole JSONB array, not only the
     * {@code primary_*} column, so "cardio" also finds an exercise whose primary body part
     * is "legs" but which is tagged cardio as well.
     *
     * <p>Written as a native query because {@code jsonb_array_elements_text} has no JPQL
     * equivalent. {@code CAST(... AS text)} is required by PostgreSQL to type the bind
     * parameter when it is null.
     */
    @Query(value = """
            SELECT * FROM exercise e
            WHERE (CAST(:name AS text) IS NULL
                   OR e.name ILIKE CONCAT('%', CAST(:name AS text), '%'))
              AND (CAST(:bodyPart AS text) IS NULL
                   OR EXISTS (SELECT 1 FROM jsonb_array_elements_text(e.body_parts) AS bp
                              WHERE bp ILIKE CAST(:bodyPart AS text)))
              AND (CAST(:equipment AS text) IS NULL
                   OR EXISTS (SELECT 1 FROM jsonb_array_elements_text(e.equipments) AS eq
                              WHERE eq ILIKE CAST(:equipment AS text)))
            ORDER BY e.name
            LIMIT :limit
            """, nativeQuery = true)
    List<Exercise> search(@Param("name") String name,
                          @Param("bodyPart") String bodyPart,
                          @Param("equipment") String equipment,
                          @Param("limit") int limit);
}
