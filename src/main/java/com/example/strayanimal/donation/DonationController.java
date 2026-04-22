package com.example.strayanimal.donation;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    /**
     * 创建捐赠（资金或物资）
     */
    @PostMapping
    public Donation create(@RequestParam(required = false) Long donorId,
                           @RequestBody Donation request) {
        return donationService.createDonation(donorId, request);
    }

    /**
     * 公开的捐赠列表（用于前台公示）
     */
    @GetMapping("/public")
    public Page<Donation> listPublic(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return donationService.listPublic(page, size);
    }

    /**
     * 后台查看所有捐赠记录
     */
    @GetMapping("/admin")
    public Page<Donation> listAdmin(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return donationService.listAdmin(page, size);
    }

    @GetMapping("/my")
    public Page<DonationSummaryDTO> listMy(@RequestParam Long donorId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return donationService.listMyDonations(donorId, page, size);
    }

    /**
     * 后台更新捐赠状态（确认/已使用等）
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Donation> updateStatus(@PathVariable Long id,
                                                 @RequestParam String status) {
        return ResponseEntity.ok(donationService.updateStatus(id, status));
    }

    /**
     * 新增捐赠使用记录
     */
    @PostMapping("/{donationId}/usages")
    public DonationUsage addUsage(@PathVariable Long donationId,
                                  @RequestBody DonationUsage request) {
        return donationService.addUsage(donationId, request);
    }

    /**
     * 查看某条捐赠的使用明细（追溯）
     */
    @GetMapping("/{donationId}/usages")
    public Page<DonationUsageDTO> listUsages(@PathVariable Long donationId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return donationService.listUsageByDonation(donationId, page, size);
    }

    @GetMapping("/usages/recent")
    public Page<DonationUsageDTO> listRecentUsages(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "6") int size) {
        return donationService.listRecentUsages(page, size);
    }
}
