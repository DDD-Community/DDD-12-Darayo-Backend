package ddd.darayo.festival.presentation.performance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ddd.darayo.festival.domain.dto.PerformanceURLContentDTO;
import ddd.darayo.festival.domain.service.PerformanceManagement;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/performance/{performanceId}/performanceURL")
@RequiredArgsConstructor
public class PerformanceUrlAdminController {

    private final PerformanceManagement performanceManagement;

    @PostMapping
    public ResponseEntity<Void> addPerformanceURL(
        @PathVariable Long performanceId,
        @RequestBody PerformanceURLContentDTO dto
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        performanceManagement.addPerformanceURL(performanceId, dto, now);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{performanceURLId}")
    public ResponseEntity<Void> deletePerformanceURL(
        @PathVariable Long performanceURLId
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        performanceManagement.deletePerformanceURL(performanceURLId, now);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{performanceURLId}")
    public ResponseEntity<Void> updatePerformanceURL(
        @PathVariable Long performanceURLId,
        @RequestBody PerformanceURLContentDTO dto
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        performanceManagement.updatePerformanceURL(performanceURLId, dto, now);
        return ResponseEntity.ok().build();
    }
}