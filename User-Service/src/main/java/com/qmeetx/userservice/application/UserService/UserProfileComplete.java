package com.qmeetx.userservice.application.UserService;

import com.qmeetx.userservice.api.dto.ProfileCompletiondto;
import com.qmeetx.userservice.domain.Enums.ProfileStatus;
import com.qmeetx.userservice.domain.model.User;
import com.qmeetx.userservice.domain.repository.UserRepository;
import com.qmeetx.userservice.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
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

        if(user.getProfileStatus()==ProfileStatus.COMPLETE){
            throw  new IllegalStateException("User profile has been completed");
        }
user.setPhone(profileCompletiondto.getPhone());
user.setDob(profileCompletiondto.getDob());
user.setGender(profileCompletiondto.getGender());

//As Profile is complete
user.setProfileStatus(ProfileStatus.COMPLETE);
userRepository.save(user);





    }
}
