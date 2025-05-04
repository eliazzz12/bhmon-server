package org.bhmon.server.model.exceptions;

public class MonNotFoundException extends RuntimeException {
    public MonNotFoundException(String message) {
        super(message);
    }
    public MonNotFoundException() {}
}
