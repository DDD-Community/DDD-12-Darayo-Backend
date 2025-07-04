package ddd.darayo.festival.domain.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserError implements ErrorInfo {
    NOT_FOUND_USER(404, "U001", "존재하지 않는 유저입니다."),
    ;

    private final int status;
    private final String code;
    private final String desc;
}
