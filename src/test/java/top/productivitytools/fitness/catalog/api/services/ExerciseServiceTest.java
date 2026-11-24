package top.productivitytools.fitness.catalog.api.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import top.productivitytools.fitness.catalog.api.dto.UpdateExerciseRequest;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.repositories.ExerciseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExerciseServiceTest {

    @Test
    void updateByExerciseIdUpdatesAllMetadataFields() {
        ExerciseRepository repo = Mockito.mock(ExerciseRepository.class);
        Exercise existing = new Exercise();
        existing.setExerciseId("fp_bench_press");
        existing.setName("bench press");
        existing.setPrimaryEquipment("body weight");
        existing.setEquipments(List.of("body weight"));

        when(repo.findByExerciseId("fp_bench_press")).thenReturn(Optional.of(existing));
        when(repo.save(any(Exercise.class))).thenAnswer(inv -> inv.getArgument(0));

        ExerciseService service = new ExerciseService(repo);
        UpdateExerciseRequest request = new UpdateExerciseRequest(
                "bench press",
                "chest",
                "barbell",
                "pectorals",
                List.of("chest"),
                List.of("barbell"),
                List.of("pectorals"),
                List.of("triceps"),
                List.of("Press the bar up."));

        Optional<Exercise> updated = service.updateByExerciseId("fp_bench_press", request);

        assertTrue(updated.isPresent());
        assertEquals("barbell", updated.get().getPrimaryEquipment());
        assertEquals(List.of("barbell"), updated.get().getEquipments());
        assertEquals("chest", updated.get().getPrimaryBodyPart());
        assertEquals("pectorals", updated.get().getPrimaryMuscle());
        assertEquals(List.of("triceps"), updated.get().getSecondaryMuscles());
        assertEquals(List.of("Press the bar up."), updated.get().getInstructions());
    }

    @Test
    void updateByExerciseIdReturnsEmptyWhenNotFound() {
        ExerciseRepository repo = Mockito.mock(ExerciseRepository.class);
        when(repo.findByExerciseId("missing")).thenReturn(Optional.empty());

        ExerciseService service = new ExerciseService(repo);
        UpdateExerciseRequest request = new UpdateExerciseRequest(
                "bench press", null, "barbell", null, null, null, null, null, null);

        assertTrue(service.updateByExerciseId("missing", request).isEmpty());
    }
}
