package com.qmeetx.authservice.application.signupService;

import com.qmeetx.authservice.api.dto.SignupRequestDTO;

public interface SignupService {

    public void signup(SignupRequestDTO signupRequestDTO);
 /*   public String createTenantId(String organizationName);
*/
}
