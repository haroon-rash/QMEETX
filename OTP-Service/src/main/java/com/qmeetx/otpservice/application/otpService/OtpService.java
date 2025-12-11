package com.qmeetx.otpservice.application.otpService;

import com.qmeetx.otpservice.domain.Models.OtpData;
import reactor.core.publisher.Mono;

public interface OtpService {


   public Mono<String> saveAndSendOtp(OtpData otpData);

public Mono<Boolean> verifyOtp(String token,String otpCode);
}
