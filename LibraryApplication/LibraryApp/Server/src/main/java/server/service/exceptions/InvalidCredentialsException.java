package server.service.exceptions;

public class InvalidCredentialsException extends Throwable {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
