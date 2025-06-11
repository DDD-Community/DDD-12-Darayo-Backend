package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.service.AuthService;
import ddd.darayo.festival.domain.service.PerformanceManagement;
import ddd.darayo.festival.presentation.performance.exchanges.PerformanceDetailRes;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Performance performance = performanceManagement.save(req);
        return ResponseEntity.ok(performance.getId());
    }

    @GetMapping
    public ResponseEntity<List<PerformanceDetailRes>> getAllPerformanceDetails() {
        return ResponseEntity.ok(performanceManagement.findAllDetail());
    }

    @DeleteMapping("/{performanceId}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long performanceId) {
        performanceManagement.delete(performanceId);
        return ResponseEntity.noContent().build();
    }
}
