package com.example.strayanimal.donation;

import com.example.strayanimal.common.BaseEntity;
import com.example.strayanimal.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "donation")
public class Donation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
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
}

