package com.example.strayanimal.adoption;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionApplicationController {

    private final AdoptionApplicationService adoptionApplicationService;

    public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService) {
        this.adoptionApplicationService = adoptionApplicationService;
    }

    /**
     * 公众提交领养申请
     */
    @PostMapping("/apply")
    public AdoptionApplication apply(@RequestParam Long applicantId,
                                     @RequestParam Long animalId,
                                     @RequestBody AdoptionApplication request) {
        return adoptionApplicationService.apply(applicantId, animalId, request);
    }

    /**
     * 申请人查看自己的申请
     */
    @GetMapping("/my")
    public Page<AdoptionApplicationDTO> myApplications(@RequestParam Long applicantId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return adoptionApplicationService.listMyApplications(applicantId, page, size);
    }

    /**
     * 后台按状态查看申请（按匹配度评分倒序）
     */
    @GetMapping("/admin")
    public Page<AdoptionAdminDTO> listForAdmin(@RequestParam String status,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return adoptionApplicationService.listByStatusForAdmin(status, page, size);
    }

    /**
     * 审核更新状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AdoptionApplication> updateStatus(@PathVariable Long id,
                                                            @RequestParam String status,
                                                            @RequestParam(required = false) String rejectReason) {
        return ResponseEntity.ok(adoptionApplicationService.updateStatus(id, status, rejectReason));
    }

    /**
     * 管理员：按当前规则重新计算所有历史申请的评分
     */
    @PostMapping("/admin/recalculate-score")
    public ResponseEntity<Void> recalculateScorePost() {
        adoptionApplicationService.recalculateAllScores();
        return ResponseEntity.noContent().build();
    }

    // 为了方便直接在浏览器中点击链接访问，这里同时支持 GET
    @GetMapping("/admin/recalculate-score")
    public ResponseEntity<Void> recalculateScoreGet() {
        adoptionApplicationService.recalculateAllScores();
        return ResponseEntity.noContent().build();
    }
}

