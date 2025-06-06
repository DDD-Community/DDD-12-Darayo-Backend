package ddd.darayo.festival.domain.exception.constant;

import ddd.darayo.festival.domain.exception.DomainException;

public enum ArtistError implements ErrorInfo{
    ARTIST_NOT_EXISTS("해당하는 아티스트가 없습니다."),

    ;

    ArtistError(String description) {
        this.description = description;
    }

    private final String description;

    @Override
    public String getDesc() {
        return description;
    }
}
