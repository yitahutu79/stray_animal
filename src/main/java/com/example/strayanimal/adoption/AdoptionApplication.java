package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.common.BaseEntity;
import com.example.strayanimal.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(name = "adoption_application")
public class AdoptionApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    @JsonIgnore // 避免序列化懒加载代理导致 500
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    @JsonIgnore // 前端目前只展示简单字段，这里先忽略整个对象
    private Animal animal;

    @Column(length = 20)
    private String status; // SUBMITTED/UNDER_REVIEW/APPROVED/REJECTED/CANCELLED

    @Lob
    private String reason; // 申请理由

    @Lob
    private String homeEnv; // 居住环境描述

    private Boolean hasOtherPet;

    @Column(length = 20)
    private String incomeLevel;

    @Lob
    private String experience; // 养宠经验

    private Integer score; // 系统匹配评分

    @Lob
    private String rejectReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getHomeEnv() {
        return homeEnv;
    }

    public void setHomeEnv(String homeEnv) {
        this.homeEnv = homeEnv;
    }

    public Boolean getHasOtherPet() {
        return hasOtherPet;
    }

    public void setHasOtherPet(Boolean hasOtherPet) {
        this.hasOtherPet = hasOtherPet;
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }

    public void setIncomeLevel(String incomeLevel) {
        this.incomeLevel = incomeLevel;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
