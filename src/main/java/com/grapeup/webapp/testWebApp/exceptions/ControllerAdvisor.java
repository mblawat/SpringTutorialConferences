package com.grapeup.webapp.testWebApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SessionException.class)
    public ResponseEntity<Object> handleSessionNotFound(SessionException ex, WebRequest request) {
        Map<String, Object> body = Map.of("timestamp", LocalDateTime.now(), "message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SpeakerException.class)
    public ResponseEntity<Object> handleSpeakerNotFound(SpeakerException ex, WebRequest request) {
        Map<String, Object> body = Map.of("timestamp", LocalDateTime.now(), "message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
