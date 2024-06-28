package com.planner.oauth.user;

import java.util.Map;

import com.planner.enums.MemberRole;
import com.planner.enums.OAuthType;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

	private final Map<String,Object> attributes;
	private final String accessToken;
	private final String id;
	private final String email;
	private final String name;
	
	public KakaoOAuth2UserInfo(String accessToken, Map<String, Object>attributes) {
		this.accessToken = accessToken;
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        this.attributes = kakaoProfile;
        
        this.id = ((Long)attributes.get("id")).toString();
        this.email = (String) kakaoAccount.get("email");
        this.name = (String) this.attributes.get("nickname");
        this.attributes.put("id", this.id);
        this.attributes.put("email", this.email);
	}
	@Override
	public OAuth2Provider getProdriver() {
		return OAuth2Provider.KAKAO;
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
		return "none";
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getType() {
		return OAuthType.KAKAO.getOAuthType();
	}
	@Override
	public String getRole() {
		return MemberRole.USER.getType();
	}

}
