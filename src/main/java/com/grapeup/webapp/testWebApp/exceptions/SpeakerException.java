package com.grapeup.webapp.testWebApp.exceptions;

public class SpeakerException extends RuntimeException {

    public SpeakerException(Long id) {
        super(String.format("Speaker with Id %d not found", id));
    }

}
