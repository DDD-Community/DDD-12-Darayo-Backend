package ddd.darayo.festival.presentation.performance;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import ddd.darayo.festival.domain.dto.ReservationInfoContentDTO;
import ddd.darayo.festival.domain.service.PerformanceManagement;

@RestController
@RequestMapping("/api/admin/performance/{performanceId}/reservation")
@RequiredArgsConstructor
public class PerformanceReservationAdminController {
    private final PerformanceManagement performanceManagement;

    @PostMapping
    public ResponseEntity<Void> addReservationInfo(
            @PathVariable("performanceId") Long performanceId,
            @RequestBody ReservationInfoContentDTO req
    ) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        performanceManagement.addReservationInfo(performanceId, req, now);
        return ResponseEntity.ok().build();
    }
}
