package ddd.darayo.festival.domain.exception.constant;

public enum PlaceError implements ErrorInfo{
    PLACE_NOT_EXIST("해당하는 장소 정보가 없습니다."),
    PLACE_HALL_NOT_EXIST("해당하는 스테이지 정보가 없습니다.")
    ;
    PlaceError(String description) {
        this.description = description;
    }
    private String description;

    @Override
    public String getDesc() {
        return description;
    }
}
