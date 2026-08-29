package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.entities.Exercise;
import mohamedjaouad.TRAINOVA.exceptions.NotFoundException;
import mohamedjaouad.TRAINOVA.recordsDTO.ExerciseImageUpdateDTO;
import mohamedjaouad.TRAINOVA.repositories.ExerciseRepository;
import mohamedjaouad.TRAINOVA.services.CloudinaryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private static final String IMAGE_FOLDER = "trainova/exercises";

    private final ExerciseRepository exerciseRepository;
    private final CloudinaryService cloudinaryService;

    public ExerciseController(ExerciseRepository exerciseRepository, CloudinaryService cloudinaryService) {
        this.exerciseRepository = exerciseRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }


    @GetMapping("/missing-image")
    public List<Exercise> getExercisesWithoutImage() {
        return exerciseRepository.findAll().stream()
                .filter(e -> e.getImageUrl() == null || e.getImageUrl().isBlank())
                .toList();
    }

    @PostMapping("/{id}/image")
    public Exercise uploadExerciseImage(@PathVariable UUID id, @RequestParam("image") MultipartFile image) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        String url = cloudinaryService.uploadImage(image, IMAGE_FOLDER);
        exercise.setImageUrl(url);
        return exerciseRepository.save(exercise);
    }
    @PutMapping("/update-images")
    public List<Exercise> updateExerciseImages(@RequestBody List<ExerciseImageUpdateDTO> updates) {
        List<Exercise> updated = new ArrayList<>();

        for (ExerciseImageUpdateDTO update : updates) {
            Exercise exercise = exerciseRepository.findAll().stream()
                    .filter(e -> e.getName().equalsIgnoreCase(update.exerciseName()))
                    .findFirst()
                    .orElse(null);

            if (exercise != null) {
                exercise.setImageUrl(update.imageUrl());
                updated.add(exerciseRepository.save(exercise));
            }
        }

        return updated;
    }
}