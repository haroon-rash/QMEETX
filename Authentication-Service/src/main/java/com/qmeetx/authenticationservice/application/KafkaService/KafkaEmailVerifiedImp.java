package com.qmeetx.authenticationservice.application.KafkaService;

import com.qmeetx.authenticationservice.api.dto.VerifiedDTO;
import com.qmeetx.authenticationservice.domain.models.User;
import com.qmeetx.authenticationservice.domain.repository.UserRepository;

import com.qmeetx.qmeetxshared.Topics.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import otp.event.EmailVerifiedEvent;

@Slf4j
@Service
public class KafkaEmailVerifiedImp implements KafkaEmailVerifiedConsumer{


    private final UserRepository userRepository;

    public KafkaEmailVerifiedImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @KafkaListener(
            topics = "email-verified-topic",
            groupId = "${spring.kafka.consumer.group-id}"
    )


    public VerifiedDTO consumeVerifiedEvent(EmailVerifiedEvent event) {

        VerifiedDTO verifiedDTO = new VerifiedDTO();
        verifiedDTO.setEmail(event.getEmail());
        verifiedDTO.setVerified(event.getIsVerified());
      User user= userRepository.findByEmail(verifiedDTO.getEmail());
if(user!=null) {
    if (verifiedDTO.getVerified()) {
        user.setVarified(true);
        userRepository.save(user);
    } else {
        log.warn("Email verification failed");
        // optional: log/alert for failed verification
    }
}
        return verifiedDTO;



    }
}
