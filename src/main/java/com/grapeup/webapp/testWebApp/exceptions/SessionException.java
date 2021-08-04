package com.grapeup.webapp.testWebApp.exceptions;

public class SessionException extends RuntimeException {

    public SessionException(Long id) {
        super(String.format("Session with Id %d not found", id));
    }

}
