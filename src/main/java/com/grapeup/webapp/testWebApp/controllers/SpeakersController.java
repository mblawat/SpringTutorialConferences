package com.grapeup.webapp.testWebApp.controllers;

import com.grapeup.webapp.testWebApp.models.Session;
import com.grapeup.webapp.testWebApp.models.Speaker;
import com.grapeup.webapp.testWebApp.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakersController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @GetMapping
    public List<Speaker> list() {
        return speakerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Speaker get(@PathVariable final Long id) {
        return speakerRepository.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Speaker create(@RequestBody final Speaker speaker) {
        return speakerRepository.saveAndFlush(speaker);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable final Long id) {
        speakerRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public Speaker update(@PathVariable final Long id, @RequestBody Speaker speaker) {
        // TODO: Validate body
        Speaker existing_speaker = speakerRepository.getById(id);
        BeanUtils.copyProperties(speaker, existing_speaker, "speaker_id");
        return speakerRepository.saveAndFlush(existing_speaker);
    }
}
