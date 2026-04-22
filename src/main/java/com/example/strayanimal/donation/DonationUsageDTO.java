package com.example.strayanimal.donation;

import com.example.strayanimal.animal.Animal;

import java.time.LocalDateTime;

public class DonationUsageDTO {

    private Long id;
    private Long donationId;
    private String donorName;
    private String donationType;
    private Double donationAmount;
    private Long animalId;
    private String animalName;
    private String projectName;
    private LocalDateTime useTime;
    private String useDesc;
    private String photoUrl;
    private LocalDateTime createTime;

    public DonationUsageDTO(Long id,
                            Long donationId,
                            String donorName,
                            String donationType,
                            Double donationAmount,
                            Long animalId,
                            String animalName,
                            String projectName,
                            LocalDateTime useTime,
                            String useDesc,
                            String photoUrl,
                            LocalDateTime createTime) {
        this.id = id;
        this.donationId = donationId;
        this.donorName = donorName;
        this.donationType = donationType;
        this.donationAmount = donationAmount;
        this.animalId = animalId;
        this.animalName = animalName;
        this.projectName = projectName;
        this.useTime = useTime;
        this.useDesc = useDesc;
        this.photoUrl = photoUrl;
        this.createTime = createTime;
    }

    public static DonationUsageDTO fromEntity(DonationUsage usage) {
        Donation donation = usage.getDonation();
        Animal animal = usage.getAnimal();
        return new DonationUsageDTO(
                usage.getId(),
                donation != null ? donation.getId() : null,
                donation != null ? donation.getDonorName() : null,
                donation != null ? donation.getType() : null,
                donation != null ? donation.getAmount() : null,
                animal != null ? animal.getId() : null,
                animal != null ? animal.getName() : null,
                usage.getProjectName(),
                usage.getUseTime(),
                usage.getUseDesc(),
                usage.getPhotoUrl(),
                usage.getCreateTime()
        );
    }

    public Long getId() { return id; }
    public Long getDonationId() { return donationId; }
    public String getDonorName() { return donorName; }
    public String getDonationType() { return donationType; }
    public Double getDonationAmount() { return donationAmount; }
    public Long getAnimalId() { return animalId; }
    public String getAnimalName() { return animalName; }
    public String getProjectName() { return projectName; }
    public LocalDateTime getUseTime() { return useTime; }
    public String getUseDesc() { return useDesc; }
    public String getPhotoUrl() { return photoUrl; }
    public LocalDateTime getCreateTime() { return createTime; }
}
