package com.example.strayanimal.donation;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation_usage")
public class DonationUsage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private Donation donation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Animal animal; // 可为空，表示用于某只动物

    @Column(length = 100)
    private String projectName; // 或项目名称，如“冬季保暖计划”

    private LocalDateTime useTime;

    @Lob
    private String useDesc;

    @Column(length = 255)
    private String photoUrl; // 使用凭证图片

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Donation getDonation() { return donation; }
    public void setDonation(Donation donation) { this.donation = donation; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public LocalDateTime getUseTime() { return useTime; }
    public void setUseTime(LocalDateTime useTime) { this.useTime = useTime; }
    public String getUseDesc() { return useDesc; }
    public void setUseDesc(String useDesc) { this.useDesc = useDesc; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
