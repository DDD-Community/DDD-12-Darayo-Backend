package ddd.darayo.festival.presentation.exception;

import ddd.darayo.festival.domain.exception.DomainException;
import ddd.darayo.festival.domain.exception.constant.ErrorInfo;
import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {
    public ErrorInfo error;
    public HttpStatus status;

    public static APIException from(DomainException ex, HttpStatus status) {
        return new APIException(ex.getMessage(), status);
    }

    public static APIException of(Exception ex, HttpStatus status) {
        return new APIException(ex.getMessage(), status);
    }

    APIException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}