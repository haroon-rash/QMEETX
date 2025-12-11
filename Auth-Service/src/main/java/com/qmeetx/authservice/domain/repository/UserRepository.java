package com.qmeetx.authservice.domain.repository;

import com.qmeetx.authservice.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);
    boolean existsByEmail(String email);
}
