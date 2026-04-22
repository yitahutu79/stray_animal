package com.example.strayanimal.adoption;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionFollowupController {

    private final AdoptionFollowupService followupService;

    public AdoptionFollowupController(AdoptionFollowupService followupService) {
        this.followupService = followupService;
    }

    /**
     * 新增某条领养申请的回访记录
     */
    @PostMapping("/{applicationId}/followups")
    public AdoptionFollowup addFollowup(@PathVariable Long applicationId,
                                        @RequestParam Long staffId,
                                        @RequestBody AdoptionFollowup request) {
        return followupService.addFollowup(applicationId, staffId, request);
    }

    /**
     * 查看某条领养申请的所有回访记录
     */
    @GetMapping("/{applicationId}/followups")
    public Page<AdoptionFollowupDTO> listFollowups(@PathVariable Long applicationId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return followupService.listByApplication(applicationId, page, size);
    }
}
