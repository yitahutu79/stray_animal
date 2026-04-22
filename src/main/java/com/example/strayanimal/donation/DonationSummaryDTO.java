package com.example.strayanimal.donation;

import java.time.LocalDateTime;

public class DonationSummaryDTO {

    private Long id;
    private String donorName;
    private String type;
    private Double amount;
    private String status;
    private Boolean publicFlag;
    private String remark;
    private LocalDateTime createTime;

    public DonationSummaryDTO(Long id, String donorName, String type, Double amount, String status, Boolean publicFlag, String remark, LocalDateTime createTime) {
        this.id = id;
        this.donorName = donorName;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.publicFlag = publicFlag;
        this.remark = remark;
        this.createTime = createTime;
    }

    public static DonationSummaryDTO fromEntity(Donation donation) {
        return new DonationSummaryDTO(
                donation.getId(),
                donation.getDonorName(),
                donation.getType(),
                donation.getAmount(),
                donation.getStatus(),
                donation.getPublicFlag(),
                donation.getRemark(),
                donation.getCreateTime()
        );
    }

    public Long getId() { return id; }
    public String getDonorName() { return donorName; }
    public String getType() { return type; }
    public Double getAmount() { return amount; }
    public String getStatus() { return status; }
    public Boolean getPublicFlag() { return publicFlag; }
    public String getRemark() { return remark; }
    public LocalDateTime getCreateTime() { return createTime; }
}
