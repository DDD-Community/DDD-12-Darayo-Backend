package ddd.darayo.festival.presentation.alarm;

import ddd.darayo.festival.application.usecase.alarm.PushGuideAlarmUseCase;
import ddd.darayo.festival.application.usecase.alarm.PushReservationAlarmUseCase;
import ddd.darayo.festival.application.usecase.alarm.PushReservationUpdateAlarmUseCase;
import ddd.darayo.festival.application.usecase.alarm.PushTimetableAlarmUseCase;
import ddd.darayo.festival.presentation.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(("/v1/alarm/test"))
@RequiredArgsConstructor
public class TestAlarmController {
    private final PushReservationAlarmUseCase pushReservationAlarmUseCase;
    private final PushTimetableAlarmUseCase pushTimetableAlarmUseCase;
    private final PushGuideAlarmUseCase pushGuideAlarmUseCase;
    private final PushReservationUpdateAlarmUseCase pushReservationUpdateAlarmUseCase;

    @GetMapping
    public ResponseEntity<BaseResponse<Void>> testAlarm(
            @RequestParam("type") String alarmType,
            @RequestParam("date") LocalDate date,
            @RequestParam("dayLeft") Integer day
    ) {
        if (day < 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.fail("7002", "dayLeft는 0보다 크거나 같아야 합니다."));
        }

        switch (alarmType) {
            case "reservation":
                pushReservationAlarmUseCase.execute(new PushReservationAlarmUseCase.Param(date, day));
                break;
            case "timetable":
                pushTimetableAlarmUseCase.execute(new PushTimetableAlarmUseCase.Param(date, day));
                break;
            case "guide":
                pushGuideAlarmUseCase.execute(new PushGuideAlarmUseCase.Param(date, day));
                break;
            case "updateReservation":
                pushReservationUpdateAlarmUseCase.execute(new PushReservationUpdateAlarmUseCase.Param(date));
                break;
            default:
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.fail("7001", "등록되지 않은 알림 타입입니다. 알림 타입은 reservation, timetable, guide"));
        }
        return ResponseEntity.ok(BaseResponse.success());
    }
}
