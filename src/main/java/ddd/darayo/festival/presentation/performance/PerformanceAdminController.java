package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.service.PerformanceManagement;
import ddd.darayo.festival.presentation.performance.exchanges.SavePerformanceReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/performance")
@RequiredArgsConstructor
public class PerformanceAdminController {
    private final PerformanceManagement performanceManagement;

    @PostMapping
    public ResponseEntity<Long> createPerformance(@RequestBody SavePerformanceReq req) {
        Performance performance = performanceManagement.save(req);
        return ResponseEntity.ok(performance.getId());
    }


}
