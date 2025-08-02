package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.dto.EditPerformanceDTO;
import ddd.darayo.festival.domain.dto.EditReservationInfoCommand;
import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.service.AuthService;
import ddd.darayo.festival.domain.service.PerformanceManagement;
import ddd.darayo.festival.presentation.performance.exchanges.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/admin/performance")
@RequiredArgsConstructor
public class PerformanceAdminController {
    private final PerformanceManagement performanceManagement;

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Long> createPerformance(@RequestBody SavePerformanceReq req) {
        if (!authService.authenticate(req.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Performance performance = performanceManagement.save(req, now);
        return ResponseEntity.ok(performance.getId());
    }

    @GetMapping
    public ResponseEntity<List<PerformanceDetailRes>> getAllPerformanceDetails() {
        return ResponseEntity.ok(performanceManagement.findAllDetail());
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<Void> updatePerformance(
            @PathVariable Long performanceId,
            @RequestBody EditPerformanceDTO req
    ) {
        performanceManagement.updatePerformance(performanceId, req);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{performanceId}/reservation")
    public ResponseEntity<Void> updateReservationInfos(
            @PathVariable Long performanceId,
            @RequestBody List<EditReservationInfoReq> reqList
    ) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        performanceManagement.updateReservationInfo(performanceId, reqList, now);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservation/{reservationInfoId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationInfoId
    ) {
        performanceManagement.deleteReservationInfo(reservationInfoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{performanceId}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long performanceId) {
        performanceManagement.delete(performanceId);
        return ResponseEntity.noContent().build();
    }
}
