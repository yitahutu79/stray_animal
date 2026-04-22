package com.example.strayanimal.adoption;

import com.example.strayanimal.user.User;

import java.time.LocalDateTime;

public class AdoptionFollowupDTO {

    private Long id;
    private Long applicationId;
    private Long staffId;
    private String staffName;
    private LocalDateTime visitTime;
    private String visitType;
    private String visitResult;
    private String note;
    private LocalDateTime createTime;

    public AdoptionFollowupDTO(Long id,
                               Long applicationId,
                               Long staffId,
                               String staffName,
                               LocalDateTime visitTime,
                               String visitType,
                               String visitResult,
                               String note,
                               LocalDateTime createTime) {
        this.id = id;
        this.applicationId = applicationId;
        this.staffId = staffId;
        this.staffName = staffName;
        this.visitTime = visitTime;
        this.visitType = visitType;
        this.visitResult = visitResult;
        this.note = note;
        this.createTime = createTime;
    }

    public static AdoptionFollowupDTO fromEntity(AdoptionFollowup followup) {
        User staff = followup.getStaff();
        return new AdoptionFollowupDTO(
                followup.getId(),
                followup.getApplication() != null ? followup.getApplication().getId() : null,
                staff != null ? staff.getId() : null,
                staff != null ? (staff.getRealName() != null && !staff.getRealName().trim().isEmpty() ? staff.getRealName() : staff.getUsername()) : null,
                followup.getVisitTime(),
                followup.getVisitType(),
                followup.getVisitResult(),
                followup.getNote(),
                followup.getCreateTime()
        );
    }

    public Long getId() { return id; }
    public Long getApplicationId() { return applicationId; }
    public Long getStaffId() { return staffId; }
    public String getStaffName() { return staffName; }
    public LocalDateTime getVisitTime() { return visitTime; }
    public String getVisitType() { return visitType; }
    public String getVisitResult() { return visitResult; }
    public String getNote() { return note; }
    public LocalDateTime getCreateTime() { return createTime; }
}
