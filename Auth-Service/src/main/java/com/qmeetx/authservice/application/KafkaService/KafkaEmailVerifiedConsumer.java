package com.qmeetx.authservice.application.KafkaService;


import com.qmeetx.authservice.api.dto.VerifiedDTO;
import otp.event.EmailVerifiedEvent;

public interface KafkaEmailVerifiedConsumer {

public VerifiedDTO consumeVerifiedEvent(EmailVerifiedEvent event);


}
