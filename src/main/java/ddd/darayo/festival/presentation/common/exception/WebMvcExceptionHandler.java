package ddd.darayo.festival.presentation.common.exception;

import ddd.darayo.festival.domain.exception.DomainException;
import ddd.darayo.festival.presentation.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class WebMvcExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<BaseResponse<Void>> handleAPIException(APIException e) {
        logException(e);

        BaseResponse<Void> response = BaseResponse.fail(e.error.getCode(), e.error.getDesc());
        return ResponseEntity.status(e.status)
                .body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<BaseResponse<Void>> handleDomainException(DomainException e) {
        logException(e);

        BaseResponse<Void> response = BaseResponse.fail(e.error.getCode(), e.error.getDesc());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handle(Exception e) {
        logException(e);

        BaseResponse<Void> response = BaseResponse.fail("9999", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private void logException(Exception e) {
        if (e.getCause() != null) {
            log.info("{} : {}", e.getCause().getClass(), e.getCause().getMessage());
        } else {
            log.info(e.getMessage());
        }
    }
}
