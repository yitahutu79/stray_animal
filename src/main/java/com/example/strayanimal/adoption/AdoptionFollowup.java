package com.example.strayanimal.adoption;

import com.example.strayanimal.common.BaseEntity;
import com.example.strayanimal.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AdoptionApplication getApplication() { return application; }
    public void setApplication(AdoptionApplication application) { this.application = application; }
    public User getStaff() { return staff; }
    public void setStaff(User staff) { this.staff = staff; }
    public LocalDateTime getVisitTime() { return visitTime; }
    public void setVisitTime(LocalDateTime visitTime) { this.visitTime = visitTime; }
    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }
    public String getVisitResult() { return visitResult; }
    public void setVisitResult(String visitResult) { this.visitResult = visitResult; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
