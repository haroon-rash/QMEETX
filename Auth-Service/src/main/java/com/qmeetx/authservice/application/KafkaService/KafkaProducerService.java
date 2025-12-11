package com.qmeetx.authservice.application.KafkaService;


public interface KafkaProducerService {

public void sendOTPEvent(String name,String email);


}
