package top.productivitytools.fitness.catalog.api.dto;

import java.util.List;

import top.productivitytools.fitness.catalog.api.entitles.Exercise;

/**
 * Lightweight view of an exercise returned by the search endpoint.
 *
 * <p>It deliberately excludes the animation bytes: a search can easily match a hundred
 * exercises and the GIFs are up to 1.2 MB each. Clients that need the animation fetch it
 * separately from {@code GET /api/exercises/{exerciseId}/image}.
 */
public record ExerciseSearchResultDto(
        String exerciseId,
        String name,
        String primaryBodyPart,
        String primaryEquipment,
        String primaryMuscle,
        List<String> bodyParts,
        List<String> equipments,
        List<String> targetMuscles,
        List<String> secondaryMuscles,
        List<String> instructions,
        String imageFileName,
        boolean hasImage) {

    public static ExerciseSearchResultDto from(Exercise exercise) {
        return new ExerciseSearchResultDto(
                exercise.getExerciseId(),
                exercise.getName(),
                exercise.getPrimaryBodyPart(),
                exercise.getPrimaryEquipment(),
                exercise.getPrimaryMuscle(),
                exercise.getBodyParts(),
                exercise.getEquipments(),
                exercise.getTargetMuscles(),
                exercise.getSecondaryMuscles(),
                exercise.getInstructions(),
                exercise.getImageFileName(),
                exercise.getImageFileName() != null);
    }
}
