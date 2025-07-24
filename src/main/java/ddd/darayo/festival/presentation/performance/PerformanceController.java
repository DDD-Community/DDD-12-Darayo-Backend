package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.application.usecase.performance.GetAlarmedFestivalUseCase;
import ddd.darayo.festival.domain.entity.UserPerformanceAlarm;
import ddd.darayo.festival.domain.exception.DomainException;
import ddd.darayo.festival.domain.repository.UserPerformanceAlarmRepository;
import ddd.darayo.festival.domain.service.AlarmSettingManagement;
import ddd.darayo.festival.domain.service.PerformanceManagement;
import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.presentation.common.BaseResponse;
import ddd.darayo.festival.presentation.common.exception.APIException;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetPerformanceInfo;
import ddd.darayo.festival.presentation.performance.exchanges.UserGetTimetableRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/festival")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceManagement performanceManagement;
    private final TimetableManagement timetableManagement;
    private final AlarmSettingManagement alarmSettingManagement;
    private final GetAlarmedFestivalUseCase getAlarmedFestivalUseCase;
    private final UserPerformanceAlarmRepository userPerformanceAlarmRepository;

    @GetMapping
    public ResponseEntity<BaseResponse<List<UserGetPerformanceInfo>>> getPerformances() {
        List<UserGetPerformanceInfo> data = performanceManagement.findUserPerformance();
        return ResponseEntity.ok(BaseResponse.success(data));
    }

    @GetMapping("/alarmed")
    public ResponseEntity<BaseResponse<List<UserGetPerformanceInfo>>> getAlarmedPerformances(
            @RequestAttribute("userId") Long userId
    ) {
        List<UserGetPerformanceInfo> result = getAlarmedFestivalUseCase.execute(new GetAlarmedFestivalUseCase.Param(userId));
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/{festivalId}/timetable")
    public ResponseEntity<BaseResponse<List<UserGetTimetableRes>>> getPerformancesForFestival(
            @PathVariable Long festivalId
    ) {
        List<UserGetTimetableRes> data = timetableManagement.getUserGetTimetables(festivalId);
        return ResponseEntity.ok(BaseResponse.success(data));
    }

    @PostMapping("/{festivalId}/push")
    public ResponseEntity<BaseResponse<Void>> enrollPerformanceAlarm(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long festivalId
    ) {
        try {
            alarmSettingManagement.enrollPerformanceAlarm(userId, festivalId);
            log.info("공연 알림 설정 성공 - 사용자: {}, 공연: {}", userId, festivalId);
            return ResponseEntity.ok(BaseResponse.success());
        } catch (DomainException e) {
            log.warn("공연 알림 설정 실패 - 사용자: {}, 공연: {}, 오류: {}", userId, festivalId, e.getMessage());
            throw new APIException(e, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{festivalId}/push")
    public ResponseEntity<BaseResponse<Boolean>> getPerformanceAlarm(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long festivalId
    ) {
        Optional<UserPerformanceAlarm> alarm = userPerformanceAlarmRepository.findByUserIdAndPerformanceId(userId, festivalId);
        return ResponseEntity.ok(BaseResponse.success(alarm.isPresent()));
    }


    @DeleteMapping("/{festivalId}/push")
    public ResponseEntity<BaseResponse<Void>> unenrollPerformanceAlarm(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long festivalId
    ) {
        try {
            alarmSettingManagement.unenrollPerformanceAlarm(userId, festivalId);
            log.info("공연 알림 해제 성공 - 사용자: {}, 공연: {}", userId, festivalId);
            return ResponseEntity.ok(BaseResponse.success());
        } catch (DomainException e) {
            log.warn("공연 알림 해제 실패 - 사용자: {}, 공연: {}, 오류: {}", userId, festivalId, e.getMessage());
            throw new APIException(e, HttpStatus.NOT_FOUND);
        }
    }
}
