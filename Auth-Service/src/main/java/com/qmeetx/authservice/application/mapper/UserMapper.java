package com.qmeetx.authservice.application.mapper;


import com.qmeetx.authservice.api.dto.SignupRequestDTO;

import com.qmeetx.authservice.domain.models.User;

public class UserMapper {
    public static User maptoUser(SignupRequestDTO signupRequestDTO) {

        User user = new User();
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(signupRequestDTO.getPassword());
        user.setName(signupRequestDTO.getName());

       /*
          user.setPhone(signupRequestDTO.getPhone());

        Organization organization =OrganizationMapper.mapToOrganization(signupRequestDTO) ;
        user.setOrganization(organization);
        user.setIndustry(signupRequestDTO.getIndustry());*/
        return user;

    }




}
