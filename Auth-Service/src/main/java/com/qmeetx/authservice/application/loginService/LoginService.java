package com.qmeetx.authservice.application.loginService;


import com.qmeetx.authservice.api.dto.LoginRequestDTO;
import com.qmeetx.authservice.domain.models.User;

public interface LoginService {

    public String ValidateLogin(LoginRequestDTO loginRequestDTO);
}
