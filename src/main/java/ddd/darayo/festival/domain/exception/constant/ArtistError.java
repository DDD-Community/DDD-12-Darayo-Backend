package ddd.darayo.festival.domain.exception.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ArtistError implements ErrorInfo{
    ARTIST_NOT_EXISTS("1000","해당하는 아티스트가 없습니다."),
    ARTIST_ALIAS_NOT_EXISTS("1001","해당 하는 아티스트 별명이 없습니다.")
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
