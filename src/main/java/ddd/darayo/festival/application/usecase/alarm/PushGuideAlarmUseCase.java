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

@Slf4j
@Service
@RequiredArgsConstructor
public class PushGuideAlarmUseCase implements UseCase<PushGuideAlarmUseCase.Param, Void> {
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
        
        log.info("반입물품/교통안내 알림 실행 - 대상 날짜: {}", targetDate);
        
        List<Performance> performances = performanceRepository.findByDate(targetDate);
        
        for (Performance performance : performances) {
            sendGuideAlarm(performance);
        }
        
        log.info("반입물품/교통안내 알림 발송 완료. 대상 공연: {}", performances.size());
        return null;
    }
    
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