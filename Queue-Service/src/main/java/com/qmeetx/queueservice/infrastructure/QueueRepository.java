package com.qmeetx.queueservice.infrastructure;

import com.qmeetx.queueservice.domain.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByOrganizationId(Long organizationId);
}
