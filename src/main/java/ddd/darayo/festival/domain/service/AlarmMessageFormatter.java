package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.constant.AlarmConstant;
import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.entity.ReservationInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlarmMessageFormatter {
    private static final String RESERVATION_INFO_UPDATE_FORMAT = "\uD83C\uDF9F\uFE0F 티켓 예매일 오픈 !";
    private static final String RESERVATION_INFO_UPDATE_DESCRIPTION_FORMAT = "%s 지금 바로 확인해보세요 \uD83C\uDFC3";


    private static final String DAY_FORMAT = "D-%s";

    private static final String RESERVATION_DUE_ALARM_TITLE_FORMAT = "\uD83C\uDF9F\uFE0F 티켓 예매 %s!";
    private static final String RESERVATION_DUE_ALARM_DESCRIPTION_FORMAT = "%s 잊지말고 준비하세요 \uD83D\uDD25";

    private static final String TIMETABLE_ALARM_TITLE = "페스티벌 D-3 !";
    private static final String TIMETABLE_ALARM_DESCRIPTION_FORMAT = "%s 타임테이블을 미리 확인하세요 \uD83D\uDCC6";

    private static final String BANNED_GOOD_AND_TRANSPORTATION_TITLE = "페스티벌 D-1 !";
    private static final String BANNED_GOOD_AND_TRANSPORTATION_DESCRIPTION_FORMAT = "%s 반입물품과 교통편을 미리 확인하세요 \uD83D\uDCE2";

    public record MessageContent(
        String title,
        String description,
        Map<String, String> data
    ) {}

    public MessageContent reservationUpdateAlarm(ReservationInfo reservationInfo) {
        return new MessageContent(
                RESERVATION_INFO_UPDATE_FORMAT,
                String.format(RESERVATION_INFO_UPDATE_DESCRIPTION_FORMAT, reservationInfo.getPerformance().getName()),
                Map.of(

                        AlarmConstant.TYPE_HEADER, "RESERVATION_UPDATE_ALARM",
                        AlarmConstant.FESTIVAL_ID_HEADER, reservationInfo.getPerformance().getId().toString()
                )
        );
    }

    public MessageContent reservationDueDateAlarm(Performance p, int day) {
        String dayMention = switch (day) {
            case 0 -> "DAY";
            default -> String.valueOf(day);
        };

        String renderedDay = String.format(DAY_FORMAT, dayMention);

        return new MessageContent(
                String.format(RESERVATION_DUE_ALARM_TITLE_FORMAT, renderedDay),
                String.format(RESERVATION_DUE_ALARM_DESCRIPTION_FORMAT, p.getName()),
                Map.of(
                        AlarmConstant.TYPE_HEADER, "RESERVATION_DUE_ALARM",
                        AlarmConstant.FESTIVAL_ID_HEADER, p.getId().toString()
                )
        );
    }

    public MessageContent performanceTimetableAlarm(Performance p) {
        return new MessageContent(
                TIMETABLE_ALARM_TITLE,
                String.format(TIMETABLE_ALARM_DESCRIPTION_FORMAT, p.getName()),
                Map.of(
                        AlarmConstant.TYPE_HEADER, "TIMETABLE_ALARM",
                        AlarmConstant.FESTIVAL_ID_HEADER, p.getId().toString()
                )
        );
    }

    public MessageContent performanceGuideAlarm(Performance p) {
        return new MessageContent(
                BANNED_GOOD_AND_TRANSPORTATION_TITLE,
                String.format(BANNED_GOOD_AND_TRANSPORTATION_DESCRIPTION_FORMAT, p.getName()),
                Map.of(
                        AlarmConstant.TYPE_HEADER, "GUIDE_ALARM",
                        AlarmConstant.FESTIVAL_ID_HEADER, p.getId().toString()
                )
        );
    }
}
