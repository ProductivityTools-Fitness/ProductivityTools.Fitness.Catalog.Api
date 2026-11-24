package top.productivitytools.fitness.catalog.api.dto;

import java.util.List;

public record UpdateExerciseRequest(
        String name,
        String primaryBodyPart,
        String primaryEquipment,
        String primaryMuscle,
        List<String> bodyParts,
        List<String> equipments,
        List<String> targetMuscles,
        List<String> secondaryMuscles,
        List<String> instructions) {
}
