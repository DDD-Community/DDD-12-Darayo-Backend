package ddd.darayo.festival.infra.aop;

import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.entity.PerformanceURL;
import ddd.darayo.festival.domain.entity.ReservationInfo;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import ddd.darayo.festival.domain.repository.PerformanceURLRepository;
import ddd.darayo.festival.domain.repository.ReservationInfoRepository;
import ddd.darayo.festival.domain.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class TouchPerformanceAspect {
    private final PerformanceRepository performanceRepository;
    private final ReservationInfoRepository reservationInfoRepository;
    private final PerformanceURLRepository performanceURLRepository;
    private final TimetableRepository timetableRepository;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(annotation)")
    public Object touchPerformance(ProceedingJoinPoint pjp, TouchPerformanceUpdatedAt annotation) throws Throwable {
        Object result = pjp.proceed();

        MethodBasedEvaluationContext ctx = new MethodBasedEvaluationContext(null,
                pjp.getSignature() instanceof org.aspectj.lang.reflect.MethodSignature ms ? ms.getMethod() : null,
                pjp.getArgs(), nameDiscoverer);

        Long performanceId = null;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Object keyVal = parser.parseExpression(annotation.key()).getValue(ctx);
        if (annotation.now() != null && !annotation.now().isBlank()) {
            Object nowVal = parser.parseExpression(annotation.now()).getValue(ctx);
            if (nowVal instanceof LocalDateTime n) {
                now = n;
            }
        }

        switch (annotation.by()) {
            case PERFORMANCE_ID -> performanceId = (Long) keyVal;
            case RESERVATION_INFO_ID -> {
                ReservationInfo info = reservationInfoRepository.findById((Long) keyVal).orElse(null);
                performanceId = info != null ? info.getPerformance().getId() : null;
            }
            case PERFORMANCE_URL_ID -> {
                PerformanceURL url = performanceURLRepository.findById((Long) keyVal).orElse(null);
                performanceId = url != null ? url.getPerformance().getId() : null;
            }
            case TIMETABLE_ID -> {
                Long timetableId = (Long) keyVal;
                var timetableOpt = timetableRepository.findById(timetableId);
                if (timetableOpt.isPresent() && timetableOpt.get().getPerformance() != null) {
                    performanceId = timetableOpt.get().getPerformance().getId();
                }
            }
            case HALL_ID -> {
                final LocalDateTime finalNow1 = now;
                List<Performance> list = performanceRepository.findByHallId((Long) keyVal);
                list.forEach(p -> p.touch(finalNow1));
                return result;
            }
            case PLACE_ID -> {
                final LocalDateTime finalNow2 = now;
                List<Performance> list = performanceRepository.findByPlace_Id((Long) keyVal);
                list.forEach(p -> p.touch(finalNow2));
                return result;
            }
        }

        if (performanceId != null) {
            final LocalDateTime finalNow = now;
            performanceRepository.findById(performanceId).ifPresent(p -> p.touch(finalNow));
        }
        return result;
    }
}


