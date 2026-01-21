package com.qmeetx.otpservice.application.otpService;

import com.qmeetx.otpservice.application.KafkaProducer.EmailVerifiedProducer;
import com.qmeetx.otpservice.application.MailService.MailService;
import com.qmeetx.otpservice.application.RedisService.RedisService;
import com.qmeetx.otpservice.domain.Models.OtpData;
import com.qmeetx.otpservice.domain.Util.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import otp.event.EmailVerifiedEvent;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class OtpServiceImp implements OtpService {

private final MailService mailService;
private final RedisService  redisService;
private final EmailVerifiedProducer emailVerifiedProducer;


public OtpServiceImp(MailService mailService, RedisService redisService, EmailVerifiedProducer emailVerifiedProducer) {
    this.mailService = mailService;
    this.redisService = redisService;
    this.emailVerifiedProducer = emailVerifiedProducer;

}



    @Override
    public Mono<String> saveAndSendOtp(OtpData otpData) {
String otp = OtpUtil.generateOtpCode();
log.info("Generated OTP Code: {}", otp); // Debug logging
otpData.setOtpCode(otp);

return  redisService.saveData(otpData)

        .flatMap(token->{
                if(token!=null)
                {
                    log.info("Redis Token : {}", token);
                   return mailService.sendOtpMail(otpData)
                    //return token to frontend so frontend store this temp
                    //token for validation of OTP

                           .thenReturn(token);

                }
                else {
                    log.error("Error sending OTP code to redis");

                    return Mono.error(new RuntimeException("Error sending OTP code to redis"));
                }
        });



    }
    @Override
    public Mono<Boolean> verifyOtp(String otpToken, String otpCode) {
        return redisService.getData(otpToken)
                .flatMap(storedOtp -> {
                    if (storedOtp != null
                            && storedOtp.getOtpCode() != null
                            && storedOtp.getOtpCode().equals(otpCode)) {

                        String email = storedOtp.getEmail();

                        return redisService.deleteData(otpToken)
                                .then(emailVerifiedProducer.sendEmailVerifiedEvent(email, true))
                                .thenReturn(true) // OTP valid
                                .onErrorResume(ex -> {
                                    log.error("Failed to publish email verified event for {}", email, ex);
                                    return Mono.just(false); // OTP valid but event publish failed
                                });

                    } else {
                        // Wrong OTP case
                        return Mono.just(false);
                    }
                })
                //  If no OTP found for this token → return false
                .switchIfEmpty(Mono.just(false));
    }


}