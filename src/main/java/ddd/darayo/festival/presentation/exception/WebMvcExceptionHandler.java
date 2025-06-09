package ddd.darayo.festival.presentation.exception;

import ddd.darayo.festival.domain.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class WebMvcExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleAPIException(APIException e) {
        logException(e);

        ErrorResponse response = new ErrorResponse(
                e.error.getCode(),
                e.error.getDesc(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(e.status).body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {
        logException(e);

        ErrorResponse response = new ErrorResponse(
                e.error.getCode(),
                e.error.getDesc(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        logException(e);

        ErrorResponse response = new ErrorResponse(
                "UNKNOWN_ERROR",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private void logException(Exception e) {
        if (e.getCause() != null) {
            log.info("{} : {}", e.getCause().getClass(), e.getCause().getMessage());
        } else {
            log.info(e.getMessage());
        }
    }
}
