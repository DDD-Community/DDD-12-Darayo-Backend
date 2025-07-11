package ddd.darayo.festival.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProviderType {
    DEVICE("DEVICE"),
    APPLE("APPLE"),
    ;

    private final String key;
}
