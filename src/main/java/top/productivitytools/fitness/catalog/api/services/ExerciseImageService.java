package top.productivitytools.fitness.catalog.api.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.entitles.ExerciseImage;
import top.productivitytools.fitness.catalog.api.repositories.ExerciseImageRepository;
import top.productivitytools.fitness.catalog.api.repositories.ExerciseRepository;

@Service
@RequiredArgsConstructor
public class ExerciseImageService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseImageRepository exerciseImageRepository;

    /** Thrown when the image refers to an exercise that has not been imported yet. */
    public static class ExerciseNotFoundException extends RuntimeException {
        public ExerciseNotFoundException(String exerciseId) {
            super("Exercise not found: " + exerciseId);
        }
    }

    /**
     * Stores the GIF for the given exercise.
     *
     * <p>Overwrites the existing row instead of inserting a second one. That is not just
     * convenience: {@code exercise_image.exercise_id} carries a UNIQUE constraint, so a
     * blind insert would fail the moment the same exercise is imported twice.
     *
     * @param exerciseId business key from exercises.json, not the numeric primary key
     */
    @Transactional
    public ExerciseImage save(String exerciseId, String fileName, String contentType, byte[] data) {
        Exercise exercise = exerciseRepository.findByExerciseId(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));

        ExerciseImage image = exerciseImageRepository.findByExercise(exercise)
                .orElseGet(ExerciseImage::new);

        image.setExercise(exercise);
        image.setFileName(fileName);
        image.setContentType((contentType != null && !contentType.isBlank()) ? contentType : "image/gif");
        image.setFileSizeBytes((long) data.length);
        image.setImageData(data);

        // Mirror the file name onto the exercise so a metadata-only query can tell
        // whether an animation is available without touching the bytes.
        exercise.setImageFileName(fileName);
        exerciseRepository.save(exercise);

        return exerciseImageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public Optional<ExerciseImage> findByExerciseId(String exerciseId) {
        return exerciseRepository.findByExerciseId(exerciseId)
                .flatMap(exerciseImageRepository::findByExercise);
    }
}
