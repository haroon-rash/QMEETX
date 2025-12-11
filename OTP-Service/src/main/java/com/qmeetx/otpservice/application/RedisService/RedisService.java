package com.qmeetx.otpservice.application.RedisService;


import com.qmeetx.otpservice.domain.Models.OtpData;
import reactor.core.publisher.Mono;

public interface RedisService {

Mono<String> saveData(OtpData otpData);
Mono<OtpData> getData(String userKey);

    Mono<Boolean> deleteData(String userKey);
}
