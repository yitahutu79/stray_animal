package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.common.BaseEntity;
import com.example.strayanimal.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}

