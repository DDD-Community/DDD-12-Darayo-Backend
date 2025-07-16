package ddd.darayo.festival.presentation.common.exception;

import ddd.darayo.festival.domain.exception.DomainException;
import ddd.darayo.festival.domain.exception.constant.ErrorInfo;
import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {
    public final ErrorInfo error;
    public HttpStatus status;

    public APIException(ErrorInfo errorInfo, HttpStatus status) {
        super(errorInfo.getMessage());
        this.error = errorInfo;
        this.status = status;
    }

    public APIException(DomainException exception, HttpStatus status) {
        super(exception.getMessage());
        this.error = exception.error;
        this.status = status;
    }
}