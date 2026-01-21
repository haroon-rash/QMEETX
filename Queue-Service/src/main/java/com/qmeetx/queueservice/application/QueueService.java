package com.qmeetx.queueservice.application;

import com.qmeetx.queueservice.domain.Queue;
import com.qmeetx.queueservice.domain.QueueToken;
import com.qmeetx.queueservice.infrastructure.QueueRepository;
import com.qmeetx.queueservice.infrastructure.QueueTokenRepository;
import com.qmeetx.queueservice.infrastructure.TokenCounterService;
import com.qmeetx.queueservice.infrastructure.QueueEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final QueueTokenRepository queueTokenRepository;
    private final TokenCounterService tokenCounterService;
    private final QueueEventProducer queueEventProducer;

    public Queue createQueue(String name, Long organizationId) {
        Queue queue = Queue.builder()
                .name(name)
                .organizationId(organizationId)
                .status(Queue.QueueStatus.CLOSED)
                .build();
        Queue savedQueue = queueRepository.save(queue);
        
        // Publish Event
        com.qmeetx.qmeetxshared.events.QueueCreatedEvent event = com.qmeetx.qmeetxshared.events.QueueCreatedEvent.builder()
                .queueId(savedQueue.getId())
                .name(savedQueue.getName())
                .organizationId(savedQueue.getOrganizationId())
                .createdAt(LocalDateTime.now())
                .build();
        queueEventProducer.publishQueueCreated(event);
        
        return savedQueue;
    }

    public Queue updateQueueStatus(Long queueId, Queue.QueueStatus status) {
        Queue queue = getQueueById(queueId);
        queue.setStatus(status);
        if (status == Queue.QueueStatus.OPEN) {
            // Optional: Reset counter if opening a fresh queue? 
            // For now, we persist numbers to avoid confusion if re-opened same day.
        }
        return queueRepository.save(queue);
    }

    @Transactional
    public QueueToken joinQueue(Long queueId) {
        Queue queue = getQueueById(queueId);
        if (queue.getStatus() != Queue.QueueStatus.OPEN) {
            throw new IllegalStateException("Queue is CLOSED");
        }

        Long tokenNumber = tokenCounterService.generateNextToken(queueId);

        QueueToken token = QueueToken.builder()
                .queueId(queueId)
                .tokenNumber(tokenNumber)
                .status(QueueToken.TokenStatus.WAITING)
                .joinedAt(LocalDateTime.now())
                .build();

        QueueToken savedToken = queueTokenRepository.save(token);

        // Publish Event
        com.qmeetx.qmeetxshared.events.TokenIssuedEvent event = com.qmeetx.qmeetxshared.events.TokenIssuedEvent.builder()
                .tokenId(savedToken.getId())
                .queueId(savedToken.getQueueId())
                .tokenNumber(savedToken.getTokenNumber())
                .issuedAt(savedToken.getJoinedAt())
                .build();
        queueEventProducer.publishTokenIssued(event);

        return savedToken;
    }

    @Transactional
    public QueueToken serveNext(Long queueId) {
        // 1. Mark current SERVING as COMPLETED
        List<QueueToken> servingTokens = queueTokenRepository.findByQueueIdAndStatus(queueId, QueueToken.TokenStatus.SERVING);
        for (QueueToken token : servingTokens) {
            token.setStatus(QueueToken.TokenStatus.COMPLETED);
            token.setCompletedAt(LocalDateTime.now());
            queueTokenRepository.save(token);
            
            // Publish Event
            com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent event = com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent.builder()
                    .tokenId(token.getId())
                    .queueId(token.getQueueId())
                    .status(token.getStatus().name())
                    .updatedAt(token.getCompletedAt())
                    .build();
            queueEventProducer.publishTokenStatusUpdated(event);
        }

        // 2. Pick next WAITING and mark SERVING
        // We use findFirst...OrderByJoinedAtAsc to ensure FIFO
        return queueTokenRepository.findFirstByQueueIdAndStatusOrderByJoinedAtAsc(queueId, QueueToken.TokenStatus.WAITING)
                .map(token -> {
                    token.setStatus(QueueToken.TokenStatus.SERVING);
                    token.setServedAt(LocalDateTime.now());
                    QueueToken savedToken = queueTokenRepository.save(token);

                    // Publish Event
                    com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent event = com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent.builder()
                            .tokenId(savedToken.getId())
                            .queueId(savedToken.getQueueId())
                            .status(savedToken.getStatus().name())
                            .updatedAt(savedToken.getServedAt())
                            .build();
                    queueEventProducer.publishTokenStatusUpdated(event);

                    return savedToken;
                })
                .orElse(null); // No one waiting
    }

    public Queue getQueueById(Long id) {
        return queueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queue not found with id: " + id));
    }
    
    public List<Queue> getQueuesByOrganization(Long organizationId) {
        return queueRepository.findByOrganizationId(organizationId);
    }

    public List<QueueToken> getWaitingTokens(Long queueId) {
        return queueTokenRepository.findByQueueIdAndStatus(queueId, QueueToken.TokenStatus.WAITING);
    }
}
