package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.service.PerformanceManagement;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/festival")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceManagement performanceManagement;

    @GetMapping
    public ResponseEntity<List<UserGetPerformanceInfo>> getPerformances() {
        return ResponseEntity.ok(performanceManagement.findUserPerformance());
    }
}
