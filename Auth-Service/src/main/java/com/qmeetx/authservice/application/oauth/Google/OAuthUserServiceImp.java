package com.qmeetx.authservice.application.oauth.Google;

import com.qmeetx.authservice.domain.enums.AuthProvider;
import com.qmeetx.authservice.domain.enums.UserRole;
import com.qmeetx.authservice.domain.models.Provider;
import com.qmeetx.authservice.domain.models.User;
import com.qmeetx.authservice.domain.repository.ProviderRepository;
import com.qmeetx.authservice.domain.repository.UserRepository;
import com.qmeetx.authservice.exceptions.OAuthProcessingException;
import com.qmeetx.authservice.infrastructure.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthUserServiceImp implements OAuthUserService {

    private final UserRepository userRepository;
    private final ProviderRepository  providerRepository;
    private final JwtTokenProvider  jwtTokenProvider;



@Override
    @Transactional
    public User OAuthLogin(OAuth2User oAuth2User){

        String email =oAuth2User.getAttribute("email");
        //Google Unique Id of every users
        String sub = oAuth2User.getAttribute("sub");
        // return true only if email is verified

        boolean emailVerified = Boolean.TRUE.equals(oAuth2User.getAttribute("email_verified"));
        if(email==null){
            throw new OAuthProcessingException("Google Account did not Return an Email");
        }
        if (sub==null){
            throw new IllegalArgumentException("Google Account did not Return an Sub");
        }
        log.info("Google Login Attempts with : {} and  providerUserId : {}", email,sub);

        User user;



        Optional <Provider> existProvider = providerRepository.findByProviderNameAndProviderUserId(AuthProvider.GOOGLE.name(),sub);
        if(existProvider.isPresent()) {
            user = existProvider.get().getUser();
            log.info("Existing Google Account-Linked with : {}", email);

        } else {
            //get 'LOCAL' USER
                user= userRepository.findByEmail(email);
        //If User is present
                if(user!=null){

             // Check this user is linked with 'GOOGLE' or not

                    boolean linked = user.getProviders().stream().anyMatch(p->AuthProvider.GOOGLE==p.getProviderName() && p.getProviderUserId().equals(sub));
             //if Not Linked Found Then Create provider
                    if(!linked){

                 Provider provider = new  Provider();
                 provider.setProviderUserId(sub);
                 provider.setProviderName(AuthProvider.GOOGLE);
                 provider.setUser(user);
                 user.getProviders().add(provider);
                 userRepository.save(user);


             }
          //if NOT USER is Found Then Create User and Provider with Null Password
        } else {
            user=new User();
            user.setEmail(email);
            user.setName(email.split("@")[0]);
            user.setRole(UserRole.OWNER);
            user.setVarified(emailVerified);


            Provider provider = new  Provider();
            provider.setProviderUserId(sub);
            provider.setUser(user);
            provider.setProviderName(AuthProvider.GOOGLE);

            user.getProviders().add(provider);

            userRepository.save(user);


        }


        }

return user;
    }




}
