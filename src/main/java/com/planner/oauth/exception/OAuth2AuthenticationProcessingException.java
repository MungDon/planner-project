package com.planner.oauth.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException     {  // 인증관련 예외발생시 처리
    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
