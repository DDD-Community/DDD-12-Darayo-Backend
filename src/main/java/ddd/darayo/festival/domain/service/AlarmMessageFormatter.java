package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.Performance;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlarmMessageFormatter {
    private static final String RESERVATION_DUE_ALARM_TITLE_FORMAT = "%s 티켓 예매가 %s 잊지말고 준비하세요 \uD83D\uDD25";
    private static final String DAY_MENTION_FORMAT = "%d일 남았어요!";
    private static final String TODAY_MENTION_FORMAT = "오늘이에요!";

    private static final String PERFORMANCE_GUIDE_ALARM_TITLE_FORMAT = "%s이(가) 내일이에요! 반입물품과 교통편을 미리 확인하세요 \uD83D\uDCE2";
    private static final String PERFORMANCE_TIMETABLE_ALARM_TITLE_FORMAT=  "%s이(가) 3일 남았어요! 타임테이블을 미리 확인해보세요 \uD83D\uDCC6";

    private static final String CLICK_DESCRIPTION = "눌러서 자세한 내용을 확인해보세요.";

    public record MessageContent(
        String title,
        String description,
        Map<String, String> data
    ) {}

    public MessageContent reservationDueDateAlarm(Performance p, int day) {
        String dayMention = switch(day) {
            case 0 -> TODAY_MENTION_FORMAT;
            default -> String.format(DAY_MENTION_FORMAT, day);
        };

        return new MessageContent(
                String.format(RESERVATION_DUE_ALARM_TITLE_FORMAT, p.getName(), dayMention),
                CLICK_DESCRIPTION,
                Map.of()
        );
    }

    public MessageContent performanceGuideAlarm(Performance p) {
        return new MessageContent(
                String.format(PERFORMANCE_GUIDE_ALARM_TITLE_FORMAT, p.getName()),
                CLICK_DESCRIPTION,
                Map.of()
        );
    }

    public MessageContent performanceTimetableAlarm(Performance p) {
        return new MessageContent(
                String.format(PERFORMANCE_TIMETABLE_ALARM_TITLE_FORMAT, p.getName()),
                CLICK_DESCRIPTION,
                Map.of()
        );
    }
}
