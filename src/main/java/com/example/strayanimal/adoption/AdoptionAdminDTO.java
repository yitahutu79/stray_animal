package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdoptionAdminDTO {

    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long animalId;
    private String animalName;
    private String status;
    private Integer score;
    private String reason;
    private String homeEnv;
    private Boolean hasOtherPet;
    private String incomeLevel;
    private String experience;
    private String evaluation;
    private LocalDateTime createTime;

    public static AdoptionAdminDTO fromEntity(AdoptionApplication app) {
        User applicant = app.getApplicant();
        Animal animal = app.getAnimal();
        int score = app.getScore() != null ? app.getScore() : 0;
        return new AdoptionAdminDTO(
                app.getId(),
                applicant != null ? applicant.getId() : null,
                applicant != null ? (applicant.getRealName() != null ? applicant.getRealName() : applicant.getUsername()) : null,
                animal != null ? animal.getId() : null,
                animal != null ? animal.getName() : null,
                app.getStatus(),
                score,
                app.getReason(),
                app.getHomeEnv(),
                app.getHasOtherPet(),
                app.getIncomeLevel(),
                app.getExperience(),
                AdoptionEvaluationUtil.buildEvaluation(app, animal, score),
                app.getCreateTime()
        );
    }
}

