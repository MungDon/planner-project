package com.planner.oauth.user;

import java.util.Map;

public interface OAuth2UserInfo {

	OAuth2Provider getProdriver();
	
	String getAccessToken();
	
	Map<String,Object> getAttributes();
	
	String getId();
	
	String getEmail();
	
	String getName();
	
	String getType();
	
	String getRole();
}
