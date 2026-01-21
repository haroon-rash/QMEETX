package com.qmeetx.queueservice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCounterService {

    private final org.springframework.data.redis.core.RedisOperations<String, String> redisTemplate;
    private static final String QUEUE_COUNTER_PREFIX = "queue:counter:";

    public Long generateNextToken(Long queueId) {
        return redisTemplate.boundValueOps(QUEUE_COUNTER_PREFIX + queueId).increment();
    }

    public void resetCounter(Long queueId) {
        redisTemplate.delete(QUEUE_COUNTER_PREFIX + queueId);
    }
}
