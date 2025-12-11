package com.qmeetx.authservice.application.KafkaService;

import com.qmeetx.authservice.api.dto.userCreationDTO;

public interface KafkaUserCreationService {

 public void sendUserCreationEvent(userCreationDTO user);





}
