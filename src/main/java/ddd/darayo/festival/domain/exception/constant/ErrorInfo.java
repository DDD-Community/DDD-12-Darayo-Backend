package ddd.darayo.festival.domain.exception.constant;

import ddd.darayo.festival.domain.exception.DomainException;

public interface ErrorInfo {
    String getDesc();

    default String getMessage() {
        return "[" + getCode() + "] " + getDesc();
    }

    default String getCode() {
        return ((Enum<?>) this).name();
    }

    default DomainException toException() {
        return new DomainException(this);
    }
}
