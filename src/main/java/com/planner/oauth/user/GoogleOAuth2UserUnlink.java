package com.planner.oauth.user;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
//TODO - unlink 기능을 사용하려면 Https 가 필수이기에 형식만 갖춰놓았다 추후 SSL발급 해볼예정
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink{
	private static final String URL = "http://oauth2.googleapis.com/revoke";
	private final RestTemplate restTemplate;
	@Override
	public void unlink(String accessToken) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", accessToken);
		restTemplate.postForObject(URL, params, String.class);
	}
	
}
