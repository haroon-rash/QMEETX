package com.qmeetx.otpservice.application.MailService;

import com.qmeetx.otpservice.domain.Models.OtpData;
import reactor.core.publisher.Mono;

public interface MailService {

    public Mono<Void> sendOtpMail(OtpData otpData);

}
