package com.qmeetx.otpservice.application.RedisService;

import com.qmeetx.otpservice.domain.Models.OtpData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class RedisServiceImp implements RedisService {

    // key = String    Value = String
private final ReactiveRedisTemplate<String,OtpData> reactiveRedisTemplate;

public RedisServiceImp(ReactiveRedisTemplate<String,OtpData> reactiveRedisTemplate) {
    this.reactiveRedisTemplate=reactiveRedisTemplate;
}

    @Override
    public Mono<String> saveData(OtpData otpData) {
        String token = UUID.randomUUID().toString();  // frontend-friendly token
        String redisKey = "otp:" + token;

        return reactiveRedisTemplate.opsForValue()
                .set(redisKey, otpData, Duration.ofMinutes(5))
                .flatMap(success -> {
                    if (success) {
                        return Mono.just(token); // return only token, not "otp:" prefix
                    } else {
                        return Mono.error(new RuntimeException("Failed to store OTP in Redis"));
                    }
                })
                .onErrorResume(DataAccessException.class, e -> {
                    log.error("Error storing OTP for {} in Redis", otpData.getEmail(), e);
                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Unexpected error storing OTP for {} with token {}", otpData.getEmail(), token, e);
                    return Mono.empty();
                });
    }


    @Override
    public Mono<OtpData> getData(String userKey) {
        String redisKey = "otp:" + userKey;

        return reactiveRedisTemplate.opsForValue().get(redisKey)
                .flatMap(value -> {
                    if (value == null) {
                        log.warn("OTP not found or expired for token {}", userKey);
                        return Mono.empty();
                    }
                    return Mono.just(value);
                })
                .onErrorResume(DataAccessException.class, e -> {
                    log.error("Redis data access error for token {}: {}", userKey, e.getMessage());
                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Unexpected error retrieving OTP for token {}: {}", userKey, e.getMessage());
                    return Mono.empty();
                });
    }



    @Override
    public Mono<Boolean> deleteData(String userKey) {
        String redisKey = "otp:" + userKey;

        return reactiveRedisTemplate.delete(redisKey)
                .map(deletedCount -> deletedCount > 0)
                .doOnSuccess(deleted -> {
                    if (deleted) {
                        log.info("Successfully deleted OTP for token {}", userKey);
                    } else {
                        log.warn("No OTP found to delete for token {}", userKey);
                    }
                })
                .onErrorResume(DataAccessException.class, e -> {
                    log.error("Redis data access error while deleting token {}: {}", userKey, e.getMessage());
                    return Mono.just(false);
                })
                .onErrorResume(e -> {
                    log.error("Unexpected error while deleting OTP for token {}: {}", userKey, e.getMessage());
                    return Mono.just(false);
                });
    }






}

