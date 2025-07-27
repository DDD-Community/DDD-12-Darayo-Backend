package ddd.darayo.festival.domain.exception.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PerformanceError implements ErrorInfo{
    PERFORMANCE_NOT_EXIST("2000","해당하는 공연 정보가 없습니다."),
    PERFORMANCE_ARTIST_ALREADY_EXISTS("2001","이미 참여하고 있는 아티스트입니다."),
    PERFORMANCE_ARTIST_NOT_EXIST("2002","해당 아티스트는 공연에 참여하지 않는 아티스트입니다."),
    PERFORMANCE_RESERVATION_INFO_NOT_EXIST("2003","해당하는 예매 정보는 존재하지 않습니다."),
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
