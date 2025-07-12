package ddd.darayo.festival.domain.exception.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AlarmError implements ErrorInfo {
    ALARM_SEND_FAILED("5000", "알림 전송에 실패했습니다."),
    ALARM_INVALID_TOKEN("5001", "유효하지 않은 알림 토큰입니다."),
    ;

    private final String code;
    private final String description;

    @Override
    public String getDesc() {
        return description;
    }

    @Override
    public String getCode() {
        return code;
    }
} 