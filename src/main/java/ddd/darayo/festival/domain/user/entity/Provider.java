package ddd.darayo.festival.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    DEVICE("DEVICE"),
    APPLE("APPLE"),
    ;

    private final String key;
}
