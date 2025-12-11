package com.qmeetx.otpservice.application.KafkaConsumer;

import auth.events.OtpEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.qmeetx.otpservice.application.otpService.OtpService;
import com.qmeetx.otpservice.domain.Models.OtpData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KafkaOTPEventConsumer {

    private final Flux<byte[]> kafkaMessageFlux;
    private final OtpService otpService;

    public KafkaOTPEventConsumer(Flux<byte[]> kafkaMessageFlux, OtpService otpService) {
        this.kafkaMessageFlux = kafkaMessageFlux;
        this.otpService = otpService;
    }

    @PostConstruct
    public void startConsuming() {
        kafkaMessageFlux
                .flatMap(this::processMessage) // Keep the reactive chain
                .onErrorContinue((error, obj) ->
                        log.error("Error while processing Kafka message: {}", error.getMessage(), error)
                )
                .subscribe();
    }

    private Mono<String> processMessage(byte[] message) {
        try {
            OtpEvent event = OtpEvent.parseFrom(message);

            OtpData otpData = new OtpData();

            otpData.setName(event.getName());
            otpData.setEmail(event.getEmail());

            log.info("Received OTP Event: {}", event);

            return otpService.saveAndSendOtp(otpData)
                    .doOnSuccess(v -> log.info("OTP sent successfully to {}", event.getEmail()));

        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to parse OTP Event", e);
            return Mono.empty(); // Skip this message
        }
    }
}
