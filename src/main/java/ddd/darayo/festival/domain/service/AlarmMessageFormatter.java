package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.Performance;
import org.springframework.stereotype.Service;

@Service
public class AlarmMessageFormatter {
    private static final String RESERVATION_DUE_ALARM_TITLE_FORMAT = "\"%s\" 예매 알림";
    private static final String RESERVATION_DUE_ALARM_DESCRIPTION_FORMAT = "%d일 후 예매가 예정되어 있습니다.";

    private static final String PERFORMANCE_GUIDE_ALARM_TITLE_FORMAT = "\"%s\" 반입 물품 및 교통 안내 알림";

    private static final String PERFORMANCE_TIMETABLE_ALARM_TITLE_FORMAT=  "\"%s\" 시간표 안내 알림";

    private static final String CLICK_DESCRIPTION = "눌러서 자세한 내용을 확인해보세요.";

    public record MessageContent(String title, String description) {}

    public MessageContent reservationDueDateAlarm(Performance p, int day) {
        return new MessageContent(
                String.format(RESERVATION_DUE_ALARM_TITLE_FORMAT, p.getName()),
                String.format(RESERVATION_DUE_ALARM_DESCRIPTION_FORMAT, day)
        );
    }

    public MessageContent performanceGuideAlarm(Performance p) {
        return new MessageContent(
                String.format(PERFORMANCE_GUIDE_ALARM_TITLE_FORMAT, p.getName()),
                CLICK_DESCRIPTION
        );
    }

    public MessageContent performanceTimetableAlarm(Performance p) {
        return new MessageContent(
                String.format(PERFORMANCE_TIMETABLE_ALARM_TITLE_FORMAT, p.getName()),
                CLICK_DESCRIPTION
        );
    }
}
