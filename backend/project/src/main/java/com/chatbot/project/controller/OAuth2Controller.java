package com.chatbot.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    @GetMapping("/naver")
    public Map<String, Object> naver(Authentication authentication) {
        return getOAuth2UserAttributes(authentication);
    }

    @GetMapping("/kakao")
    public Map<String, Object> kakao(Authentication authentication) {
        return getOAuth2UserAttributes(authentication);
    }

    private Map<String, Object> getOAuth2UserAttributes(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            return token.getPrincipal().getAttributes();
        }
        return Collections.singletonMap("error", "OAuth2 인증 정보가 없습니다.");
    }
}
