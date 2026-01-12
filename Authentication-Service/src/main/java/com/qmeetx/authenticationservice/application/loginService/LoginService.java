package com.qmeetx.authenticationservice.application.loginService;


import com.qmeetx.authenticationservice.api.dto.LoginRequestDTO;

public interface LoginService {

    public String ValidateLogin(LoginRequestDTO loginRequestDTO);
}
