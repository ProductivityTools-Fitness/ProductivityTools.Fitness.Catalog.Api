package top.productivitytools.fitness.exercises.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/api/debug")
public class DebugController {
    @GetMapping ("/hello")
    public String hello(){
        return "Hello";
    }
}
