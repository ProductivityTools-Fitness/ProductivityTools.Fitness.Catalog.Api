package top.productivitytools.fitness.catalog.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.entitles.ExerciseImage;

public interface ExerciseImageRepository extends JpaRepository<ExerciseImage, Long> {

    /*
     * Queried by the Exercise object rather than by a findByExerciseId(...) method.
     * The latter would be ambiguous here: Exercise has both a numeric "id" and a
     * business key named "exerciseId", so it would not be obvious which one is meant.
     */
    Optional<ExerciseImage> findByExercise(Exercise exercise);
}
