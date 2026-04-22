package com.example.strayanimal.donation;

import com.example.strayanimal.common.BaseEntity;
import com.example.strayanimal.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(name = "donation")
public class Donation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    @JsonIgnore
    private User donor;

    @Column(length = 50)
    private String donorName; // 展示用，可匿名

    @Column(length = 20)
    private String type; // MONEY / ITEM

    private Double amount; // 资金金额

    @Column(length = 20)
    private String status; // PENDING/CONFIRMED/USED/PARTLY_USED

    private Boolean publicFlag; // 是否公开

    @Lob
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getDonor() { return donor; }
    public void setDonor(User donor) { this.donor = donor; }
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getPublicFlag() { return publicFlag; }
    public void setPublicFlag(Boolean publicFlag) { this.publicFlag = publicFlag; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
