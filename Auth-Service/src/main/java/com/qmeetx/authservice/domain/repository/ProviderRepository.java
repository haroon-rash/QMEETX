package com.qmeetx.authservice.domain.repository;

import com.qmeetx.authservice.domain.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {
    Optional<Provider> findByProviderNameAndProviderUserId(String providerName, String providerUserId);
}
