package com.grapeup.webapp.testWebApp.controllers;

import com.grapeup.webapp.testWebApp.exceptions.SpeakerException;
import com.grapeup.webapp.testWebApp.models.Speaker;
import com.grapeup.webapp.testWebApp.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakersController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @GetMapping
    public Iterable<Speaker> list() {
        return speakerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Speaker get(@PathVariable final Long id) {
        Optional<Speaker> existingSpeaker = speakerRepository.findById(id);
        if(existingSpeaker.isPresent()) {
            return existingSpeaker.get();
        } else {
            throw new SpeakerException(id);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Speaker create(@RequestBody final Speaker speaker) {
        return speakerRepository.save(speaker);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        speakerRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public Speaker update(@PathVariable final Long id, @RequestBody Speaker speaker) {
        // TODO: Validate body
        Optional<Speaker> existingSpeakerOpt = speakerRepository.findById(id);
        if(existingSpeakerOpt.isPresent()) {
            Speaker existingSpeaker = existingSpeakerOpt.get();
            BeanUtils.copyProperties(speaker, existingSpeaker, "speaker_id");
            return speakerRepository.save(existingSpeaker);
        } else {
            throw new SpeakerException(id);
        }
    }
}
