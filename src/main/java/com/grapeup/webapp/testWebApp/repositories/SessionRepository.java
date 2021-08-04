package com.grapeup.webapp.testWebApp.repositories;

import com.grapeup.webapp.testWebApp.models.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
