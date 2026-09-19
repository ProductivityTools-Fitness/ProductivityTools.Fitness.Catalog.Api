package top.productivitytools.fitness.catalog.api.services;

import lombok.RequiredArgsConstructor;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.repositories.ExerciseRepository;

@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository repository;

    public Exercise add(Exercise exercise)
    {
        Exercise result=repository.save(exercise);
        return result;
    }
}
