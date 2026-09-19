package top.productivitytools.fitness.catalog.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.services.ExerciseService;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

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
}
