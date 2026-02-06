package com.example.strayanimal.adoption;

import com.example.strayanimal.common.BaseEntity;
import com.example.strayanimal.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "adoption_followup")
public class AdoptionFollowup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private AdoptionApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private User staff;

    private LocalDateTime visitTime;

    @Column(length = 20)
    private String visitType; // PHONE/HOME/ONLINE 等

    @Lob
    private String visitResult;

    @Lob
    private String note;
}

