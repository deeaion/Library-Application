package server.service.exceptions;

public class UserAlreadyLoggedInException extends Throwable {
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
