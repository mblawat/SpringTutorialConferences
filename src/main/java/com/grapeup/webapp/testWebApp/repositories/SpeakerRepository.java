package com.grapeup.webapp.testWebApp.repositories;

import com.grapeup.webapp.testWebApp.models.Speaker;
import org.springframework.data.repository.CrudRepository;

public interface SpeakerRepository extends CrudRepository<Speaker, Long> {
}
