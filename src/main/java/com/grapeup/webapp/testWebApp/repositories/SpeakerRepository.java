package com.grapeup.webapp.testWebApp.repositories;

import com.grapeup.webapp.testWebApp.models.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
}
