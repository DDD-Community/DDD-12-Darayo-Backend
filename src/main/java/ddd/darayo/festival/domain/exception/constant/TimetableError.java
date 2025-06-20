package ddd.darayo.festival.domain.exception.constant;

public enum TimetableError implements ErrorInfo {
    TIMETABLE_NOT_EXISTS("해당 하는 타임 테이블이 존재하지 않습니다."),
    TIMETABLE_ARTIST_NOT_EXISTS("해당하는 시간표에 참여하는 아티스트가 존재하지 않습니다.")
    ;

    private final String description;

    TimetableError(String description) {
        this.description = description;
    }

    @Override
    public String getDesc() {
        return this.description;
    }
}
