package com.qmeetx.queueservice.infrastructure;

import com.qmeetx.queueservice.domain.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueTokenRepository extends JpaRepository<QueueToken, Long> {
    List<QueueToken> findByQueueIdAndStatus(Long queueId, QueueToken.TokenStatus status);
    Optional<QueueToken> findFirstByQueueIdAndStatusOrderByJoinedAtAsc(Long queueId, QueueToken.TokenStatus status);
    long countByQueueIdAndStatus(Long queueId, QueueToken.TokenStatus status);
}
