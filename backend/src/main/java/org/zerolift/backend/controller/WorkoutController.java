package org.zerolift.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zerolift.backend.model.Exercise;
import org.zerolift.backend.model.Workout;
import org.zerolift.backend.repository.ExerciseRepository;
import org.zerolift.backend.repository.WorkoutRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = "*")
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public WorkoutController(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    @PostMapping
    public Workout createWorkout(@RequestBody WorkoutRequest request) {
        Workout workout = new Workout();
        workout.setTitle(request.title());
        workout.setDescription(request.description());

        if (request.exerciseIds() != null) {
            List<Exercise> exercises = request.exerciseIds().stream()
                    .map(id -> exerciseRepository.findById(UUID.fromString(id)).orElseThrow())
                    .toList();
            workout.getExercises().addAll(exercises);
        }

        return workoutRepository.save(workout);
    }

    public record WorkoutRequest(String title, String description, List<String> exerciseIds) {}
}