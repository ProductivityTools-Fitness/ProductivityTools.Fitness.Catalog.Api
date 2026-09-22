package top.productivitytools.fitness.catalog.api.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import top.productivitytools.fitness.catalog.api.dto.ExerciseSearchResultDto;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.entitles.ExerciseImage;
import top.productivitytools.fitness.catalog.api.services.ExerciseImageService;
import top.productivitytools.fitness.catalog.api.services.ExerciseService;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseImageService exerciseImageService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Exercise add(@RequestBody Exercise exercise) {
        return exerciseService.add(exercise);
    }

    @GetMapping
    public List<Exercise> getAll() {
        return exerciseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getById(@PathVariable Long id) {
        return exerciseService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Searches the catalog. All three criteria are optional and combine with AND.
     *
     * <p>Example: {@code /api/exercises/search?name=plank&bodyPart=waist&limit=20}
     */
    @GetMapping("/search")
    public List<ExerciseSearchResultDto> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String bodyPart,
            @RequestParam(required = false) String equipment,
            @RequestParam(defaultValue = "50") int limit) {
        return exerciseService.search(name, bodyPart, equipment, limit).stream()
                .map(ExerciseSearchResultDto::from)
                .toList();
    }

    /**
     * Looks an exercise up by the business key from exercises.json (for example
     * {@code exdb_plank}), as opposed to {@link #getById(Long)} which uses the generated
     * numeric id. Consumers that came from a search only know the business key.
     */
    @GetMapping("/by-exercise-id/{exerciseId}")
    public ResponseEntity<ExerciseSearchResultDto> getByExerciseId(@PathVariable String exerciseId) {
        return exerciseService.findByExerciseId(exerciseId)
                .map(ExerciseSearchResultDto::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Uploads the animation for an exercise. Addressed by the business key from
     * exercises.json (for example {@code exdb_plank}), because the importer knows that
     * value up front and does not have to remember the generated numeric id.
     *
     * <p>Sending the file again simply replaces the stored one.
     */
    @PostMapping(value = "/{exerciseId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadImage(@PathVariable String exerciseId,
                                           @RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is empty");
        }

        String fileName = (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank())
                ? file.getOriginalFilename()
                : exerciseId + ".gif";

        try {
            ExerciseImage saved = exerciseImageService.save(
                    exerciseId, fileName, file.getContentType(), file.getBytes());
            return Map.of(
                    "exerciseId", exerciseId,
                    "fileName", saved.getFileName(),
                    "fileSizeBytes", saved.getFileSizeBytes());
        } catch (ExerciseImageService.ExerciseNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{exerciseId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable String exerciseId) {
        return exerciseImageService.findByExerciseId(exerciseId)
                .map(image -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(image.getContentType()))
                        .body(image.getImageData()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
