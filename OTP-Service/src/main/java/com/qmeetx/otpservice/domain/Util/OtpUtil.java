package com.qmeetx.otpservice.domain.Util;

import java.security.SecureRandom;

public class OtpUtil {

    private static final SecureRandom random = new SecureRandom();

      // This is used to generate 6 digit otp
    public static String generateOtpCode() {
        int OtpCode =100000+ random.nextInt(900000);

   return  String.valueOf(OtpCode);
    }


}
