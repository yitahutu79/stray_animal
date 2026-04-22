package com.example.strayanimal.stats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/admin/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/overview")
    public DashboardStats overview() {
        return statsService.overview();
    }

    @GetMapping("/analytics")
    public StatsAnalyticsResponse analytics(@RequestParam(defaultValue = "MONTH") String period) {
        return statsService.analytics(period);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam(defaultValue = "MONTH") String period) {
        byte[] csv = statsService.exportAnalyticsCsv(period);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stats-" + period.toLowerCase() + ".csv")
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }
}
