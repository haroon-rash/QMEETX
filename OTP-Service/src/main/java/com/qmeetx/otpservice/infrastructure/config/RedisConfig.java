package com.qmeetx.otpservice.infrastructure.config;

import com.qmeetx.otpservice.domain.Models.OtpData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public ReactiveRedisTemplate<String, OtpData> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<OtpData> serializer = new Jackson2JsonRedisSerializer<>(OtpData.class);

        RedisSerializationContext<String, OtpData> context = RedisSerializationContext
                .<String, OtpData>newSerializationContext(new StringRedisSerializer())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
