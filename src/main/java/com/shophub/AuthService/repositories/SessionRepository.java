package com.shophub.AuthService.repositories;

import com.shophub.AuthService.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
    Session save(Session session);
}
