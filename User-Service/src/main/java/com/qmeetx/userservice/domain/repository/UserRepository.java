package com.qmeetx.userservice.domain.repository;

import com.qmeetx.userservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {


}
