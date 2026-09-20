package top.productivitytools.fitness.catalog.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> findByExerciseId(String exerciseId);

    boolean existsByExerciseId(String exerciseId);
}
