package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.service.PerformanceManagement;
import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.presentation.common.BaseResponse;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetTimetableRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/festival")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceManagement performanceManagement;
    private final TimetableManagement timetableManagement;

    @GetMapping
    public ResponseEntity<BaseResponse<List<UserGetPerformanceInfo>>> getPerformances() {
        List<UserGetPerformanceInfo> data = performanceManagement.findUserPerformance();
        return ResponseEntity.ok(BaseResponse.success(data));
    }

    @GetMapping("/{festivalId}/timetable")
    public ResponseEntity<BaseResponse<List<UserGetTimetableRes>>> getPerformancesForFestival(
            @PathVariable Long festivalId
    ) {
        List<UserGetTimetableRes> data = timetableManagement.getUserGetTimetables(festivalId);
        return ResponseEntity.ok(BaseResponse.success(data));
    }
}
