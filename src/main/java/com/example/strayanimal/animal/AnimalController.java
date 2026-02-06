package com.example.strayanimal.animal;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public Page<Animal> list(@RequestParam(required = false) String status,
                             @RequestParam(required = false) String species,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        return animalService.list(status, species, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> detail(@PathVariable Long id) {
        return animalService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Animal create(@RequestBody Animal animal) {
        return animalService.save(animal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> update(@PathVariable Long id, @RequestBody Animal animal) {
        return animalService.findById(id)
                .map(exist -> {
                    animal.setId(id);
                    return ResponseEntity.ok(animalService.save(animal));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

