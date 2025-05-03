package org.bhmon.server.model.exceptions;

public class NoRejectionsLeftException extends RuntimeException {
    public NoRejectionsLeftException(String message) {
        super(message);
    }

}
