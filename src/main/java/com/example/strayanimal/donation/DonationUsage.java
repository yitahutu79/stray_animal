package com.example.strayanimal.donation;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
}

