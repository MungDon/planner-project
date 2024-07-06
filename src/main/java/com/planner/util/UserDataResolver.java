package com.planner.util;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.planner.dto.response.member.ResMemberDetail;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;
import com.planner.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDataResolver implements HandlerMethodArgumentResolver{

	private final MemberService memberService;
	private final OAuth2Service oAuth2Service;
	
	
	// 파라미터에 어노테이션이 있는지 없는지에따라 값 반환
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(UserData.class) != null;
	}

	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		
		if(principal instanceof OAuth2UserPrincipal) {
			OAuth2UserPrincipal oAuth2UserPrincipal = (OAuth2UserPrincipal)principal;
			ResMemberDetail member = oAuth2Service.findByOAuthId(oAuth2UserPrincipal.getOAuthId());
			return member;
		}else if(principal instanceof User) {
			User user = (User)principal;
			ResMemberDetail member = memberService.formMember(user.getUsername());
			return member;
		}
		
		return null;
	}

}
