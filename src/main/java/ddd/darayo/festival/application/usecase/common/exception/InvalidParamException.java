package ddd.darayo.festival.application.usecase.common.exception;

public class InvalidParamException extends RuntimeException {
    public InvalidParamError error;
    public InvalidParamException(InvalidParamError error) {
        super(error.getMessage());
        this.error = error;
    }
}
