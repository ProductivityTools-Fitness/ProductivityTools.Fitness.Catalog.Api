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

    /** Upper bound for the number of rows a single search may return. */
    public static final int MAX_SEARCH_LIMIT = 200;

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

    @Transactional(readOnly = true)
    public Optional<Exercise> findByExerciseId(String exerciseId) {
        return exerciseRepository.findByExerciseId(exerciseId);
    }

    /**
     * Searches the catalog. Blank criteria are treated as "not given", so a request with no
     * parameters at all simply returns the first {@code limit} exercises ordered by name.
     *
     * @param limit clamped to 1..{@value #MAX_SEARCH_LIMIT} so that a client cannot ask for
     *              the whole catalog in one call
     */
    @Transactional(readOnly = true)
    public List<Exercise> search(String name, String bodyPart, String equipment, int limit) {
        return exerciseRepository.search(
                normalize(name),
                normalize(bodyPart),
                normalize(equipment),
                Math.clamp(limit, 1, MAX_SEARCH_LIMIT));
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
