package com.qmeetx.authservice.application.loginService;

import com.qmeetx.authservice.api.dto.LoginRequestDTO;
import com.qmeetx.authservice.domain.models.User;
import com.qmeetx.authservice.domain.repository.UserRepository;
import com.qmeetx.authservice.exceptions.PasswordNotMatchException;
import com.qmeetx.authservice.exceptions.UserNotFoundException;
import com.qmeetx.authservice.infrastructure.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LoginServiceImp implements LoginService {

    private final JwtTokenProvider  jwtTokenProvider;
    private final UserRepository  userRepository;
private final PasswordEncoder passwordEncoder;
    public LoginServiceImp(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String ValidateLogin(LoginRequestDTO loginRequestDTO) {

      User user= userRepository.findByEmail(loginRequestDTO.getEmail());

      if(user==null){
          throw new UserNotFoundException("User with this Email is Not Exist :  "+loginRequestDTO.getEmail());

           }
     if (!passwordEncoder.matches(loginRequestDTO.getPassword(),user.getPassword())){
         throw new PasswordNotMatchException("password is Invalid ,please Try with correct Password");
     }


     return jwtTokenProvider.generateToken(user.getEmail(),user.getName(),user.isVarified(),"admin");


    }
}
