package ddd.darayo.festival.domain.exception;

import ddd.darayo.festival.domain.exception.constant.UserError;

public class UserException extends DomainException {
    public UserException(UserError error) {
        super(error);
    }
}
