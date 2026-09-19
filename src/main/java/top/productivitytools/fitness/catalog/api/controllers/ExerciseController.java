package top.productivitytools.fitness.catalog.api.controllers;

import top.productivitytools.fitness.catalog.api.entitles.Exercise;
import top.productivitytools.fitness.catalog.api.services.ExerciseService;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


@RestController 
@RequestMapping ("/api/workout")
@RequiredArgsConstructor
public class ExerciseController {


    private final ExerciseService exerciseService;

    @PostMapping  ("/add")
    public Exercise hello(@RequestBody Exercise exercise) {

        var r=this.exerciseService.add(exercise);
        return r;
    }
}
