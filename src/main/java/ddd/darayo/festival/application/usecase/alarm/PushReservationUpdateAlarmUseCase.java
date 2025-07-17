package ddd.darayo.festival.application.usecase.alarm;

import ddd.darayo.festival.application.usecase.common.Params;
import ddd.darayo.festival.application.usecase.common.UseCase;
import ddd.darayo.festival.domain.entity.ReservationInfo;
import ddd.darayo.festival.domain.repository.ReservationInfoRepository;
import ddd.darayo.festival.domain.repository.UserAlarmTokenRepository;
import ddd.darayo.festival.domain.repository.projection.AlarmTokenProjection;
import ddd.darayo.festival.domain.service.AlarmMessageFormatter;
import ddd.darayo.festival.domain.service.PushAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushReservationUpdateAlarmUseCase implements UseCase<PushReservationUpdateAlarmUseCase.Param, Void> {

    private final ReservationInfoRepository reservationInfoRepository;
    private final UserAlarmTokenRepository userAlarmTokenRepository;
    private final PushAlarmService pushAlarmService;
    private final AlarmMessageFormatter alarmMessageFormatter;

    public record Param(LocalDate today) implements Params {}

    @Override
    @Transactional
    public Void execute(Param param) {
        // 1. 기준 시간 계산 (전날 19:00:00 ~ 오늘 19:00:00)
        LocalDateTime fromDate = param.today.minusDays(1).atTime(19, 0, 0);
        LocalDateTime toDate = param.today.atTime(19, 0, 0);

        log.info("예매정보 업데이트 알림 - 기준 구간: {} ~ {}", fromDate, toDate);

        // 2. 해당 구간에 업데이트된 예약정보 조회
        List<ReservationInfo> updatedInfos = reservationInfoRepository.findUpdatedReservationInfos(fromDate, toDate);

        if (updatedInfos.isEmpty()) {
            log.info("해당 구간에 업데이트된 예매정보 없음");
            return null;
        }

        // 3. 공연 ID별로 ReservationInfo 그룹핑
        Map<Long, List<ReservationInfo>> infosByPerformanceId = updatedInfos.stream()
                .collect(Collectors.groupingBy(info -> info.getPerformance().getId()));

        // 4. 공연 ID 목록 추출
        List<Long> performanceIds = new ArrayList<>(infosByPerformanceId.keySet());
        if (performanceIds.isEmpty()) {
            log.info("알림 발송 대상 공연 없음");
            return null;
        }

        // 5. 공연별 알림 토큰 일괄 조회 (N+1 방지)
        List<AlarmTokenProjection> allTokens = userAlarmTokenRepository.findAlarmTokens(performanceIds);
        Map<Long, List<String>> tokensByPerformanceId = allTokens.stream()
                .collect(Collectors.groupingBy(
                        AlarmTokenProjection::getTargetId,
                        Collectors.mapping(AlarmTokenProjection::getAlarmToken, Collectors.toList())
                ));

        // 6. 각 공연별로 알림 발송
        for (Long performanceId : performanceIds) {
            List<ReservationInfo> infos = infosByPerformanceId.get(performanceId);
            List<String> tokens = tokensByPerformanceId.getOrDefault(performanceId, Collections.emptyList());
            if (tokens.isEmpty()) {
                log.info("공연 ID {}: 알림 토큰 없음, 예약일 업데이트 알림 발송 생략", performanceId);
                continue;
            }
            for (ReservationInfo info : infos) {
                var messageContent = alarmMessageFormatter.reservationUpdateAlarm(info);
                PushAlarmService.MessageDTO messageDTO = new PushAlarmService.MessageDTO(
                        messageContent.title(),
                        messageContent.description(),
                        tokens,
                        messageContent.data()
                );
                pushAlarmService.sendAlarm(messageDTO);
                log.info("예매정보 업데이트 알림 발송: performanceId={}, reservationInfoId={}, tokenCount={}", performanceId, info.getId(), tokens.size());
            }
        }
        return null;
    }
}
