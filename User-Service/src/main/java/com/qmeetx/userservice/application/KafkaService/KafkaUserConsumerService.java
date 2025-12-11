package com.qmeetx.userservice.application.KafkaService;

import com.qmeetx.userservice.api.dto.UserCreationdto;
import com.qmeetx.userservice.domain.model.User;

public interface KafkaUserConsumerService {

     User consumeUserCreationEvent(UserCreationdto userCreationdto);

}
