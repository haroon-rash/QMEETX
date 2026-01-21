package com.qmeetx.authenticationservice.application.KafkaService;


import com.qmeetx.authenticationservice.api.dto.VerifiedDTO;
import otp.event.EmailVerifiedEvent;

public interface KafkaEmailVerifiedConsumer {

public     void consumeVerifiedEvent(byte[] message);


}
