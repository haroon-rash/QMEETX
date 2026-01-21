package com.qmeetx.authenticationservice.application.loginService;


import com.qmeetx.authenticationservice.api.dto.LoginRequestDTO;
import com.qmeetx.authenticationservice.domain.enums.UserRole;
import com.qmeetx.authenticationservice.domain.models.User;
import com.qmeetx.authenticationservice.domain.repository.UserRepository;
import com.qmeetx.authenticationservice.exceptions.PasswordNotMatchException;
import com.qmeetx.authenticationservice.exceptions.UserNotFoundException;
import com.qmeetx.authenticationservice.infrastructure.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
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


     return jwtTokenProvider.generateToken(user.getId().toString(),user.getEmail(),user.getName(),user.isVarified(), UserRole.OWNER.name());


    }
}
