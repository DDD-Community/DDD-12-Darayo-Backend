package ddd.darayo.festival.presentation.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {
    private static final String SUCCESS_MESSAGE = "Success";
    private static final String SUCCESS_CODE = "0000";

    public static <T> BaseResponse<T> success(T result) {
        return new BaseResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

    public static BaseResponse<Void> success() {
        return success(null);
    }

    public static BaseResponse<Void> fail(String code, String msg) {
        return new BaseResponse<Void>(code, msg, null);
    }

    private final String resultCode;
    private final String resultMsg;
    private final T result;
}
