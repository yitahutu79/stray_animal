package com.example.strayanimal.animal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Page<Animal> findByStatus(String status, Pageable pageable);

    Page<Animal> findByStatusAndSpecies(String status, String species, Pageable pageable);

    long countByStatus(String status);
}

