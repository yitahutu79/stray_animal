package com.example.strayanimal.animal;

import com.example.strayanimal.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "animal")
public class Animal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 20)
    private String species; // 狗/猫/其他

    @Column(length = 50)
    private String breed;

    @Column(length = 10)
    private String gender;

    private Integer age; // 按月或年，自行约定

    private Double weight;

    @Column(length = 100)
    private String color;

    @Column(length = 200)
    private String healthStatus;

    private Boolean vaccinated;

    private Boolean neutered;

    @Column(length = 200)
    private String characterFeature;

    @Column(length = 20)
    private String status; // 待领养/领养中/已领养/特别照顾

    @Lob
    private String description;
}

