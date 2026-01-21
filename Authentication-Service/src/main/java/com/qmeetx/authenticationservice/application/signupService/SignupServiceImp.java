package com.qmeetx.authenticationservice.application.signupService;

import com.qmeetx.authenticationservice.api.dto.SignupRequestDTO;
import com.qmeetx.authenticationservice.api.dto.userCreationDTO;
import com.qmeetx.authenticationservice.application.KafkaService.KafkaProducerService;
import com.qmeetx.authenticationservice.application.KafkaService.KafkaUserCreationService;
import com.qmeetx.authenticationservice.application.mapper.UserMapper;
import com.qmeetx.authenticationservice.domain.enums.AuthProvider;
import com.qmeetx.authenticationservice.domain.enums.UserRole;
import com.qmeetx.authenticationservice.domain.models.Provider;
import com.qmeetx.authenticationservice.domain.models.User;
import com.qmeetx.authenticationservice.domain.repository.ProviderRepository;
import com.qmeetx.authenticationservice.domain.repository.UserRepository;
import com.qmeetx.authenticationservice.exceptions.EmailAlreadyExistException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service

public class SignupServiceImp implements SignupService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaUserCreationService kafkaUserCreationService;

    public SignupServiceImp(UserRepository userRepository, ProviderRepository providerRepository, PasswordEncoder passwordEncoder, KafkaProducerService kafkaProducerService, KafkaUserCreationService kafkaUserCreationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaUserCreationService = kafkaUserCreationService;
    }
/*

@Override
public String createTenantId(String organizationName){


        for(int i=0;i<10;i++){
            String tenantId= TenantIdGenerator.generateTenantId(organizationName);
           if(!organizationRepository.existsByTenantId(tenantId))
               return tenantId;
        }
throw new  RuntimeException("Something went wrong, Error in Generating TenantId..");
}
*/

    @Override
    @Transactional
    @Async
    public void signup(SignupRequestDTO signupRequestDTO) {
if(userRepository.existsByEmail(signupRequestDTO.getEmail())) {
    throw new EmailAlreadyExistException("Email Already Exist");
}

User user= UserMapper.maptoUser(signupRequestDTO);

user.setPassword(passwordEncoder.encode(user.getPassword()));
user.setRole(UserRole.OWNER);
        Provider provider=new Provider();
        provider.setUser(user);
        provider.setProviderName(AuthProvider.LOCAL);
        provider.setProviderUserId(user.getEmail());
        user.getProviders().add(provider);




        userRepository.save(user);
        
        // Send User Creation Event
        userCreationDTO userCreationDTO = new userCreationDTO();
        userCreationDTO.setAuthId(user.getId().toString());
        userCreationDTO.setUsername(user.getName());
        userCreationDTO.setEmail(user.getEmail());
        userCreationDTO.setRole(user.getRole().name());
        userCreationDTO.setIsVerified(user.isVarified());
        
        try {
            kafkaUserCreationService.sendUserCreationEvent(userCreationDTO);
            System.out.println("User Creation Event Sent Successfully by SignupService");
        } catch (Exception e) {
            System.err.println("Failed to send User Creation Event: " + e.getMessage());
            e.printStackTrace();
        }

        kafkaProducerService.sendOTPEvent(user.getName(),user.getEmail());

    }

}
