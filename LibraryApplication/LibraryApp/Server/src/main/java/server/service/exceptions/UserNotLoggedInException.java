package server.service.exceptions;

public class UserNotLoggedInException extends Throwable {
    public UserNotLoggedInException(String message) {
        super(message);
    }
}
