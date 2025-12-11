package com.qmeetx.userservice.application.mapper;

import com.qmeetx.userservice.api.dto.UserCreationdto;
import com.qmeetx.userservice.domain.Enums.Source;
import com.qmeetx.userservice.domain.Enums.UserRoles;
import com.qmeetx.userservice.domain.model.User;


public class userCreationMapper {




    public static User maptoUser(UserCreationdto userCreationdto){

        User user=new User();

        user.setAuthId(userCreationdto.getAuthId());
        user.setEmail(userCreationdto.getEmail());
        user.setName(userCreationdto.getUsername());
        user.setIsVerified(userCreationdto.getIsVerified());
        user.setRole(UserRoles.valueOf(userCreationdto.getRole()));
        user.setSource(Source.valueOf(userCreationdto.getSource()));
return  user;
    }



}
