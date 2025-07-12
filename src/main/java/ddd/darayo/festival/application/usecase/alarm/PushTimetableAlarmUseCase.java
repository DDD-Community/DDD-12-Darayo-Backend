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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushTimetableAlarmUseCase implements UseCase<PushTimetableAlarmUseCase.Param, Void> {
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
        
        log.info("공연 시간표 안내 알림 스케줄러 실행 - 대상 날짜: {}", targetDate);
        
        List<Performance> performances = performanceRepository.findByDate(targetDate);
        
        if (performances.isEmpty()) {
            log.info("공연 시간표 안내 알림 대상 공연 없음");
            return null;
        }
        
        // 모든 공연 ID를 한번에 수집
        List<Long> performanceIds = performances.stream()
                .map(Performance::getId)
                .toList();
        
        // 한번의 쿼리로 모든 알림 토큰 조회
        List<AlarmTokenProjection> allTokens = userAlarmTokenRepository.findAlarmTokens(performanceIds);
        
        // 공연별로 알림 토큰 그룹핑
        Map<Long, List<AlarmTokenProjection>> tokensByPerformance = allTokens.stream()
                .collect(Collectors.groupingBy(AlarmTokenProjection::getTargetId));
        
        // 각 공연에 대해 알림 발송
        for (Performance performance : performances) {
            List<AlarmTokenProjection> tokens = tokensByPerformance.get(performance.getId());
            sendTimetableAlarm(performance, tokens);
        }
        
        log.info("공연 시간표 안내 알림 발송 완료. 대상 공연: {}", performances.size());
        return null;
    }
    
    private void sendTimetableAlarm(Performance performance, List<AlarmTokenProjection> tokens) {
        try {
            if (tokens == null || tokens.isEmpty()) {
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
} 