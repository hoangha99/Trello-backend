package com.trello.secutity;

import com.trello.model.AuthenticationProvider;
import com.trello.model.User;
import com.trello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2User);

        Optional<User> user = userRepository.getUserByEmail(customOAuth2User.getEmail());

        if(user.isPresent()){
            user.get().setAuthProvider(AuthenticationProvider.GOOGLE);
            user.get().setFullName(customOAuth2User.getName());
            userRepository.save(user.get());
        }
        else{
            User us = new User();
            us.setEmail(customOAuth2User.getEmail());
            us.setFullName(customOAuth2User.getName());
            us.setAuthProvider(AuthenticationProvider.GOOGLE);
            us.setPassword("");
            us.setRoleId(1l);
            userRepository.save(us);
        }
        return customOAuth2User;
    }


}
