package ddd.darayo.festival.application.scheduler;

import ddd.darayo.festival.application.usecase.alarm.PushGuideAlarmUseCase;
import ddd.darayo.festival.application.usecase.alarm.PushReservationAlarmUseCase;
import ddd.darayo.festival.application.usecase.alarm.PushTimetableAlarmUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmScheduler {
    
    private final PushReservationAlarmUseCase pushReservationAlarmUseCase;
    private final PushTimetableAlarmUseCase pushTimetableAlarm;
    private final PushGuideAlarmUseCase pushGuideAlarmUseCase;

    /**
     * 예매 1일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void pushPerformanceReservationOneDayLeft() {
        LocalDate today = LocalDate.now();
        PushReservationAlarmUseCase.Param param = new PushReservationAlarmUseCase.Param(today, 1);
        pushReservationAlarmUseCase.execute(param);
    }

    /**
     * 예매 3일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void pushPerformanceReservationThreeDayLeft() {
        LocalDate today = LocalDate.now();
        PushReservationAlarmUseCase.Param param = new PushReservationAlarmUseCase.Param(today, 3);
        pushReservationAlarmUseCase.execute(param);
    }

    /**
     * 예매 7일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void pushPerformanceReservationSevenDayLeft() {
        LocalDate today = LocalDate.now();
        PushReservationAlarmUseCase.Param param = new PushReservationAlarmUseCase.Param(today, 7);
        pushReservationAlarmUseCase.execute(param);
    }

    /**
     * 예매 당일 오전 7시 알림
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void pushPerformanceReservationToday() {
        LocalDate today = LocalDate.now();
        PushReservationAlarmUseCase.Param param = new PushReservationAlarmUseCase.Param(today, 0);
        pushReservationAlarmUseCase.execute(param);
    }

    /**
     * 공연 시간표 안내 3일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void pushPerformanceTimetable() {
        LocalDate today = LocalDate.now();
        PushTimetableAlarmUseCase.Param param = new PushTimetableAlarmUseCase.Param(today, 3);
        pushTimetableAlarm.execute(param);
    }

    /**
     * 반입물품/교통안내 1일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void pushPerformanceGuide() {
        LocalDate today = LocalDate.now();
        PushGuideAlarmUseCase.Param param = new PushGuideAlarmUseCase.Param(today, 1);
        pushGuideAlarmUseCase.execute(param);
    }
}
