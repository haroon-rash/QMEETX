package com.qmeetx.authservice.application.oauth.Google;

import com.qmeetx.authservice.domain.models.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthUserService {

    public User OAuthLogin(OAuth2User oAuth2User);

}
