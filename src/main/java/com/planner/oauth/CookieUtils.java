package com.planner.oauth;

import java.util.Base64;
import java.util.Optional;

import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {
	// 쿠키에 객체를 직렬화하여 저장하고 역직렬화 하여 불러오는 기능을 구현한 클래스
	public static Optional<Cookie> getCookie(HttpServletRequest request , String name){
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}
	
	// 응답 정보에 특정 쿠키를 추가시켜준다
	public static void addCookie(HttpServletResponse response, String name, String value, int MaxAge) {
		Cookie cookie = new Cookie(name,value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(MaxAge);
		response.addCookie(cookie);
	}
	
	// 쿠키 삭제
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}
	
	public static String serialize(Object object) {
		return Base64.getUrlEncoder()
							  .encodeToString(SerializationUtils.serialize(object));
	}
	
	public static <T> T deserialize(Cookie cookie,Class<T> cls) {
		return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
	}
}
