package com.grapeup.webapp.testWebApp.controllers;

import com.grapeup.webapp.testWebApp.exceptions.SessionException;
import com.grapeup.webapp.testWebApp.models.Session;
import com.grapeup.webapp.testWebApp.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    private final SessionRepository sessionRepository;

    public SessionsController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public Iterable<Session> list() {
        return sessionRepository.findAll();
    }

    @GetMapping("{id}")
    public Session get(@PathVariable final Long id) {
        Optional<Session> existingSession = sessionRepository.findById(id);
        if(existingSession.isPresent()) {
            return existingSession.get();
        } else {
            throw new SessionException(id);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody final Session session) {
        return sessionRepository.save(session);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        sessionRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public Session update(@PathVariable final Long id, @RequestBody Session session) {
        // TODO: Validate body
        Optional<Session> existingSessionOpt = sessionRepository.findById(id);
        if(existingSessionOpt.isPresent()) {
            Session existingSession = existingSessionOpt.get();
            BeanUtils.copyProperties(session, existingSession, "session_id");
            return sessionRepository.save(existingSession);
        } else {
            throw new SessionException(id);
        }
    }

}
