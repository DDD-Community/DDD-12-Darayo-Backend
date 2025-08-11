package ddd.darayo.festival.presentation.performance;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import ddd.darayo.festival.domain.dto.EditReservationInfoCommand;
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
        performanceManagement.addReservationInfo(performanceId, req);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reservationInfoId}")
    public ResponseEntity<Void> updateReservationInfo(
            @PathVariable("reservationInfoId") Long reservationInfoId,
            @RequestBody EditReservationInfoCommand req
    ) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        performanceManagement.updateReservationInfo(reservationInfoId, req, now);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reservationInfoId}")
    public ResponseEntity<Void> deleteReservationInfo(
            @PathVariable("reservationInfoId") Long reservationInfoId
    ) {
        performanceManagement.deleteReservationInfo(reservationInfoId);
        return ResponseEntity.ok().build();
    }
}
