package top.productivitytools.fitness.catalog.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.repositories.ExerciseRepository;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise add(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Transactional(readOnly = true)
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Exercise> findById(Long id) {
        return exerciseRepository.findById(id);
    }
}
