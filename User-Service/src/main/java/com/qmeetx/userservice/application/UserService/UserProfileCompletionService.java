package com.qmeetx.userservice.application.UserService;

import com.qmeetx.userservice.api.dto.ProfileCompletiondto;

import java.util.UUID;

public interface UserProfileCompletionService {

    void profileCompletion(UUID auidid, ProfileCompletiondto profileCompletiondto);



}
