package com.planner.oauth.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.planner.oauth.user.OAuth2UserInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserPrincipal implements OAuth2User, UserDetails {

	private final OAuth2UserInfo userInfo;

	@Override
	public String getPassword() {
		return String.valueOf(UUID.randomUUID());
	}

	@Override
	public String getUsername() {
		return userInfo.getEmail();
	}

	public String getOAuthId() {
		return userInfo.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return userInfo.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole()));
	}

	@Override
	public String getName() {
		return userInfo.getName();
	}

	public String getType() {
		return userInfo.getType();
	}
	public String getRole() {
		return userInfo.getRole();
	}

	public OAuth2UserInfo userInfo() {
		return userInfo;
	}

}
