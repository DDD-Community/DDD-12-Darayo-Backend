package ddd.darayo.festival.domain.exception;

import ddd.darayo.festival.domain.exception.constant.ErrorInfo;

public class DomainException extends RuntimeException {
    public final ErrorInfo error;

    public DomainException(ErrorInfo error) {
        super(error.getMessage());
        this.error = error;
    }
}
