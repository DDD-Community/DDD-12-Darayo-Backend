package ddd.darayo.festival.domain.exception.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlaceError implements ErrorInfo{
    PLACE_NOT_EXIST("3000", "해당하는 장소 정보가 없습니다."),
    PLACE_HALL_NOT_EXIST("3001","해당하는 스테이지 정보가 없습니다.")
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
