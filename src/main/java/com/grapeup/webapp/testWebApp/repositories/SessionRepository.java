package com.grapeup.webapp.testWebApp.repositories;

import com.grapeup.webapp.testWebApp.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
