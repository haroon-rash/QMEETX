package com.qmeetx.userservice.application.mapper;

import com.qmeetx.userservice.api.dto.UserCreationdto;
import com.qmeetx.userservice.domain.Enums.Source;
import com.qmeetx.userservice.domain.Enums.UserRoles;
import com.qmeetx.userservice.domain.model.User;
import com.qmeetx.userservice.exceptions.WrongInputFormatException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Slf4j
public class userCreationMapper {




    public static User maptoUser(UserCreationdto userCreationdto){

        User user=new User();
try {
    user.setAuthId(UUID.fromString(userCreationdto.getAuthId()));
}catch(IllegalArgumentException arg){
    log.error(arg.getMessage());
    throw new WrongInputFormatException("input Format is incorrect as Convert from String to UUID"+arg.getMessage());
}
        user.setEmail(userCreationdto.getEmail());
        user.setName(userCreationdto.getUsername());
        user.setIsVerified(userCreationdto.getIsVerified());

        try {
            user.setRole(UserRoles.valueOf(userCreationdto.getRole()));
            user.setSource(Source.valueOf(userCreationdto.getSource()));
        }catch(IllegalArgumentException arg){
            log.error(arg.getMessage());
            throw new WrongInputFormatException(arg.getMessage());
        }

        return  user;
    }



}
