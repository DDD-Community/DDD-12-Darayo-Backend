package ddd.darayo.festival.domain.exception.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TimetableError implements ErrorInfo {
    TIMETABLE_NOT_EXISTS("4000", "해당 하는 타임 테이블이 존재하지 않습니다."),
    TIMETABLE_ARTIST_NOT_EXISTS("4001","해당하는 시간표에 참여하는 아티스트가 존재하지 않습니다.")
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
