package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdoptionApplicationDTO {

    private Long id;
    private String animalName;
    private String status;
    private Integer score;
    private LocalDateTime createTime;

    public static AdoptionApplicationDTO fromEntity(AdoptionApplication app) {
        Animal animal = app.getAnimal();
        String animalName = animal != null ? animal.getName() : null;
        return new AdoptionApplicationDTO(
                app.getId(),
                animalName,
                app.getStatus(),
                app.getScore(),
                app.getCreateTime()
        );
    }
}

