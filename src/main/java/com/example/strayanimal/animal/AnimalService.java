package com.example.strayanimal.animal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Page<Animal> list(String status, String species, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String normalizedStatus = status != null && !status.trim().isEmpty() ? status.trim() : null;
        String normalizedSpecies = species != null && !species.trim().isEmpty() ? species.trim() : null;

        if (normalizedStatus != null && normalizedSpecies != null) {
            return animalRepository.findByStatusAndSpecies(normalizedStatus, normalizedSpecies, pageable);
        } else if (normalizedStatus != null) {
            return animalRepository.findByStatus(normalizedStatus, pageable);
        } else if (normalizedSpecies != null) {
            return animalRepository.findBySpecies(normalizedSpecies, pageable);
        } else {
            return animalRepository.findAll(pageable);
        }
    }

    public Optional<Animal> findById(Long id) {
        return animalRepository.findById(id);
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    public void delete(Long id) {
        animalRepository.deleteById(id);
    }
}
