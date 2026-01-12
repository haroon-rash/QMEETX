package com.qmeetx.userservice.application.UserService;

import com.qmeetx.userservice.api.dto.ProfileCompletiondto;
import com.qmeetx.userservice.domain.Enums.ProfileStatus;
import com.qmeetx.userservice.domain.model.User;
import com.qmeetx.userservice.domain.repository.UserRepository;
import com.qmeetx.userservice.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Consumer;
@Transactional
@Service
public class UserProfileComplete implements UserProfileCompletionService {

    private final UserRepository  userRepository;

    public UserProfileComplete(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

@Transactional
    @Override
    public void profileCompletion(UUID authId, ProfileCompletiondto profileCompletiondto) {

    User user = userRepository.findByAuthId(authId)
            .orElseThrow(() -> new UserNotFoundException(
                    "User not found with authId: " + authId));

     
    updateIfNotBlank(profileCompletiondto.getPhone(), user::setPhone);
    updateIfNotBlank(profileCompletiondto.getName(), user::setName);
    updateIfNotBlank(profileCompletiondto.getGender(), user::setGender);

    updateIfNotNull(profileCompletiondto.getDob(), user::setDob);

    userRepository.save(user);
//As Profile is complete
    if(user.getEmail()!=null&&user.getPhone()!=null&&user.getDob()!=null&&user.getGender()!=null&&user.getName()!=null) {

        user.setProfileStatus(ProfileStatus.COMPLETE);
        userRepository.save(user);
    }

    }

     // Use for String to check update is not blank and null
    private void updateIfNotBlank(String value , Consumer<String> setter)
    {

        if(value!=null&&!value.isBlank()){
            setter.accept(value);
        }
    }

    // Use for Objects   (Not Null) such as LocalDate Enums or any Objects

    private <T> void updateIfNotNull(T value ,Consumer<T> setter)
    {
        if(value!=null){
            setter.accept(value);
        }
    }

}
