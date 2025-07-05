package ddd.darayo.festival.application.scheduler;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.UserAlarmTokenRepository;
import ddd.darayo.festival.domain.repository.projection.AlarmTokenProjection;
import ddd.darayo.festival.domain.service.AlarmMessageFormatter;
import ddd.darayo.festival.domain.service.PushAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmScheduler {
    
    private final PerformanceRepository performanceRepository;
    private final UserAlarmTokenRepository userAlarmTokenRepository;
    private final AlarmMessageFormatter alarmMessageFormatter;
    private final PushAlarmService pushAlarmService;

    /**
     * 예매 1일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    @Transactional(readOnly = true)
    public void pushPerformanceReservationOneDayLeft() {
        log.info("예매 1일전 알림 스케줄러 실행");
        
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime tomorrowEnd = tomorrow.plusDays(1).minusSeconds(1);
        
        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(tomorrow, tomorrowEnd);
        
        for (Performance performance : performances) {
            sendReservationAlarm(performance, 1);
        }
        
        log.info("예매 1일전 알림 발송 완료. 대상 공연: {}", performances.size());
    }

    /**
     * 예매 3일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    @Transactional(readOnly = true)
    public void pushPerformanceReservationThreeDayLeft() {
        log.info("예매 3일전 알림 스케줄러 실행");
        
        LocalDateTime threeDaysLater = LocalDateTime.now().plusDays(3);
        LocalDateTime threeDaysLaterEnd = threeDaysLater.plusDays(1).minusSeconds(1);
        
        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(threeDaysLater, threeDaysLaterEnd);
        
        for (Performance performance : performances) {
            sendReservationAlarm(performance, 3);
        }
        
        log.info("예매 3일전 알림 발송 완료. 대상 공연: {}", performances.size());
    }

    /**
     * 예매 7일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    @Transactional(readOnly = true)
    public void pushPerformanceReservationSevenDayLeft() {
        log.info("예매 7일전 알림 스케줄러 실행");
        
        LocalDateTime sevenDaysLater = LocalDateTime.now().plusDays(7);
        LocalDateTime sevenDaysLaterEnd = sevenDaysLater.plusDays(1).minusSeconds(1);
        
        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(sevenDaysLater, sevenDaysLaterEnd);
        
        for (Performance performance : performances) {
            sendReservationAlarm(performance, 7);
        }
        
        log.info("예매 7일전 알림 발송 완료. 대상 공연: {}", performances.size());
    }

    /**
     * 예매 당일 오전 7시 알림
     */
    @Scheduled(cron = "0 0 7 * * ?")
    @Transactional(readOnly = true)
    public void pushPerformanceReservationToday() {
        log.info("예매 당일 알림 스케줄러 실행");
        
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime todayEnd = today.plusDays(1).minusSeconds(1);
        
        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(today, todayEnd);
        
        for (Performance performance : performances) {
            sendReservationAlarm(performance, 0);
        }
        
        log.info("예매 당일 알림 발송 완료. 대상 공연: {}", performances.size());
    }

    /**
     * 공연 시간표 안내 3일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    @Transactional(readOnly = true)
    public void pushPerformanceTimetable() {
        log.info("공연 시간표 안내 알림 스케줄러 실행");
        
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);
        List<Performance> performances = performanceRepository.findByDate(threeDaysLater);
        
        for (Performance performance : performances) {
            sendTimetableAlarm(performance);
        }
        
        log.info("공연 시간표 안내 알림 발송 완료. 대상 공연: {}", performances.size());
    }

    /**
     * 반입물품/교통안내 1일전 오후 7시 알림
     */
    @Scheduled(cron = "0 0 19 * * ?")
    @Transactional(readOnly = true)
    public void pushPerformanceGuide() {
        log.info("반입물품/교통안내 알림 스케줄러 실행");
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Performance> performances = performanceRepository.findByDate(tomorrow);
        
        for (Performance performance : performances) {
            sendGuideAlarm(performance);
        }
        
        log.info("반입물품/교통안내 알림 발송 완료. 대상 공연: {}", performances.size());
    }
    
    /**
     * 예매 알림 발송
     */
    private void sendReservationAlarm(Performance performance, int daysLeft) {
        try {
            List<AlarmTokenProjection> tokens = userAlarmTokenRepository.findAlarmTokens(List.of(performance.getId()));
            
            if (tokens.isEmpty()) {
                log.debug("공연 '{}' 예매 알림 대상 없음", performance.getName());
                return;
            }
            
            AlarmMessageFormatter.MessageContent content = alarmMessageFormatter.reservationDueDateAlarm(performance, daysLeft);
            
            List<String> tokenList = tokens.stream()
                    .map(AlarmTokenProjection::getAlarmToken)
                    .toList();
            
            PushAlarmService.MessageDTO messageDTO = new PushAlarmService.MessageDTO(
                    content.title(),
                    content.description(),
                    tokenList,
                    content.data()
            );
            
            pushAlarmService.sendAlarm(messageDTO);
            log.debug("공연 '{}' 예매 {}일전 알림 발송 완료. 대상: {}명", performance.getName(), daysLeft, tokens.size());
            
        } catch (Exception e) {
            log.error("공연 '{}' 예매 알림 발송 실패", performance.getName(), e);
        }
    }
    
    /**
     * 시간표 알림 발송
     */
    private void sendTimetableAlarm(Performance performance) {
        try {
            List<AlarmTokenProjection> tokens = userAlarmTokenRepository.findAlarmTokens(List.of(performance.getId()));
            
            if (tokens.isEmpty()) {
                log.debug("공연 '{}' 시간표 알림 대상 없음", performance.getName());
                return;
            }
            
            AlarmMessageFormatter.MessageContent content = alarmMessageFormatter.performanceTimetableAlarm(performance);
            
            List<String> tokenList = tokens.stream()
                    .map(AlarmTokenProjection::getAlarmToken)
                    .toList();
            
            PushAlarmService.MessageDTO messageDTO = new PushAlarmService.MessageDTO(
                    content.title(),
                    content.description(),
                    tokenList,
                    content.data()
            );
            
            pushAlarmService.sendAlarm(messageDTO);
            log.debug("공연 '{}' 시간표 알림 발송 완료. 대상: {}명", performance.getName(), tokens.size());
            
        } catch (Exception e) {
            log.error("공연 '{}' 시간표 알림 발송 실패", performance.getName(), e);
        }
    }
    
    /**
     * 반입물품/교통안내 알림 발송
     */
    private void sendGuideAlarm(Performance performance) {
        try {
            List<AlarmTokenProjection> tokens = userAlarmTokenRepository.findAlarmTokens(List.of(performance.getId()));
            
            if (tokens.isEmpty()) {
                log.debug("공연 '{}' 반입물품/교통안내 알림 대상 없음", performance.getName());
                return;
            }
            
            AlarmMessageFormatter.MessageContent content = alarmMessageFormatter.performanceGuideAlarm(performance);
            
            List<String> tokenList = tokens.stream()
                    .map(AlarmTokenProjection::getAlarmToken)
                    .toList();
            
            PushAlarmService.MessageDTO messageDTO = new PushAlarmService.MessageDTO(
                    content.title(),
                    content.description(),
                    tokenList,
                    content.data()
            );
            
            pushAlarmService.sendAlarm(messageDTO);
            log.debug("공연 '{}' 반입물품/교통안내 알림 발송 완료. 대상: {}명", performance.getName(), tokens.size());
            
        } catch (Exception e) {
            log.error("공연 '{}' 반입물품/교통안내 알림 발송 실패", performance.getName(), e);
        }
    }
}
