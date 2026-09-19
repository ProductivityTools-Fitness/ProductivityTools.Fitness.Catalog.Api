package top.productivitytools.fitness.catalog.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;;

@Repository 
public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
} 
