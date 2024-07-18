package com.planner.oauth.user;

import java.util.Map;

import com.planner.enums.MemberRole;
import com.planner.enums.OAuthType;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

	private final Map<String, Object> attributes;
	private final String accessToken;
	private final String id;
	private final String email;
	private final String name;
	private final String user_role;

	public GoogleOAuth2UserInfo(String accessToken, Map<String, Object> attributes, String user_role) {
		this.accessToken = accessToken;
		this.attributes = attributes;
		this.id = (String) attributes.get("sub");
		this.email = (String) attributes.get("email");
		this.name = (String) attributes.get("name");
		this.user_role = user_role;
	}

	@Override
	public OAuth2Provider getProdriver() {
		return OAuth2Provider.GOOGLE;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return OAuthType.GOOGLE.getOAuthType();
	}

	public String getRole() {
		return user_role;
	}
}
