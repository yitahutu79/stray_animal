package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.user.User;

import java.time.LocalDateTime;

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

    public AdoptionAdminDTO(Long id,
                            Long applicantId,
                            String applicantName,
                            Long animalId,
                            String animalName,
                            String status,
                            Integer score,
                            String reason,
                            String homeEnv,
                            Boolean hasOtherPet,
                            String incomeLevel,
                            String experience,
                            String evaluation,
                            LocalDateTime createTime) {
        this.id = id;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.animalId = animalId;
        this.animalName = animalName;
        this.status = status;
        this.score = score;
        this.reason = reason;
        this.homeEnv = homeEnv;
        this.hasOtherPet = hasOtherPet;
        this.incomeLevel = incomeLevel;
        this.experience = experience;
        this.evaluation = evaluation;
        this.createTime = createTime;
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getHomeEnv() { return homeEnv; }
    public void setHomeEnv(String homeEnv) { this.homeEnv = homeEnv; }
    public Boolean getHasOtherPet() { return hasOtherPet; }
    public void setHasOtherPet(Boolean hasOtherPet) { this.hasOtherPet = hasOtherPet; }
    public String getIncomeLevel() { return incomeLevel; }
    public void setIncomeLevel(String incomeLevel) { this.incomeLevel = incomeLevel; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
