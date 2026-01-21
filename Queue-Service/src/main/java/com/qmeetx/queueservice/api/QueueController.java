package com.qmeetx.queueservice.api;

import com.qmeetx.queueservice.application.QueueService;
import com.qmeetx.queueservice.domain.Queue;
import com.qmeetx.queueservice.domain.QueueToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping
    public ResponseEntity<Queue> createQueue(@RequestParam String name, @RequestParam Long organizationId) {
        return ResponseEntity.ok(queueService.createQueue(name, organizationId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Queue> updateStatus(@PathVariable Long id, @RequestParam Queue.QueueStatus status) {
        return ResponseEntity.ok(queueService.updateQueueStatus(id, status));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<QueueToken> joinQueue(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.joinQueue(id));
    }

    @PostMapping("/{id}/serve-next")
    public ResponseEntity<QueueToken> serveNext(@PathVariable Long id) {
        QueueToken token = queueService.serveNext(id);
        if (token == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Queue>> getQueuesByOrganization(@PathVariable Long organizationId) {
        return ResponseEntity.ok(queueService.getQueuesByOrganization(organizationId));
    }

    @GetMapping("/{id}/waiting")
    public ResponseEntity<List<QueueToken>> getWaitingTokens(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.getWaitingTokens(id));
    }
}
