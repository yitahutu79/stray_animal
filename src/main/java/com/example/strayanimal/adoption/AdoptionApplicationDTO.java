package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;

import java.time.LocalDateTime;

public class AdoptionApplicationDTO {

    private Long id;
    private String animalName;
    private String status;
    private Integer score;
    private LocalDateTime createTime;

    public AdoptionApplicationDTO(Long id, String animalName, String status, Integer score, LocalDateTime createTime) {
        this.id = id;
        this.animalName = animalName;
        this.status = status;
        this.score = score;
        this.createTime = createTime;
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
