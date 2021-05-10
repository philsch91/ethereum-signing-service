package at.schunker.mt.exception;

import at.schunker.mt.dto.User;

public class AuthenticationForbiddenException extends RuntimeException {

    public AuthenticationForbiddenException(){
        super("Incorrect address or password");
    }

    public AuthenticationForbiddenException (String message) {
        super(message);
    }

    public AuthenticationForbiddenException (User user) {
        super(user.getAddress() + " forbidden");
    }
}
