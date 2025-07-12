package ddd.darayo.festival.application.usecase.alarm;

import ddd.darayo.festival.application.usecase.common.Params;
import ddd.darayo.festival.application.usecase.common.UseCase;
import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.UserAlarmTokenRepository;
import ddd.darayo.festival.domain.repository.projection.AlarmTokenProjection;
import ddd.darayo.festival.domain.service.AlarmMessageFormatter;
import ddd.darayo.festival.domain.service.PushAlarmService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushReservationAlarmUseCase implements UseCase<PushReservationAlarmUseCase.Param, Void> {
    private final PerformanceRepository performanceRepository;
    private final UserAlarmTokenRepository userAlarmTokenRepository;
    private final AlarmMessageFormatter alarmMessageFormatter;
    private final PushAlarmService pushAlarmService;

    public record Param(
        LocalDate today,
        int daysLeft
    ) implements Params { }

    @Override
    @Transactional
    public Void execute(Param params) {
        LocalDate targetDate = params.today.plusDays(params.daysLeft);
        
        // LocalDate 00:00:00 기준으로 정확한 날짜 범위 계산
        LocalDateTime targetStart = targetDate.atStartOfDay();
        LocalDateTime targetEnd = targetDate.atTime(LocalTime.MAX);
        
        log.info("예매 {}일전 알림 실행 - 대상 날짜: {}", params.daysLeft, targetDate);
        
        List<Performance> performances = performanceRepository.findByReservationOpenDateBetween(targetStart, targetEnd);
        
        for (Performance performance : performances) {
            sendReservationAlarm(performance, params.daysLeft);
        }
        
        log.info("예매 {}일전 알림 발송 완료. 대상 공연: {}", params.daysLeft, performances.size());
        return null;
    }
    
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
}
