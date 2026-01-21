package com.qmeetx.organizationservice.application;

import com.qmeetx.organizationservice.domain.Organization;
import com.qmeetx.organizationservice.infrastructure.OrganizationRepository;
import com.qmeetx.organizationservice.infrastructure.OrganizationEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final com.qmeetx.organizationservice.infrastructure.OrganizationEventProducer organizationEventProducer;

    public Organization createOrganization(Organization organization) {
        Organization savedOrg = organizationRepository.save(organization);
        
        // Publish Event
        com.qmeetx.qmeetxshared.events.OrganizationCreatedEvent event = com.qmeetx.qmeetxshared.events.OrganizationCreatedEvent.builder()
                .organizationId(savedOrg.getId())
                .name(savedOrg.getName())
                .code(savedOrg.getCode())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        organizationEventProducer.publishOrganizationCreated(event);
        
        return savedOrg;
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
