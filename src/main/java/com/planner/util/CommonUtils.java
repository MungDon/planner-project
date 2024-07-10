package com.planner.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;

import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.exception.RestCustomException;
import com.planner.oauth.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

	/* 세션 쿠키 삭제 메서드 */
	public static void removeCookiesAndSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		CookieUtils.deleteCookie(request, response, "oauth2_auth_request");
		CookieUtils.deleteCookie(request, response, "mode");
		CookieUtils.deleteCookie(request, response, "redirect_uri");
	}

	/* null 체크 메서드 */
	public static boolean isEmpty(Object data) {
		if (data == null) {
			return true;
		}
		if (data instanceof String && "".equals(data)) {
			return true;
		}
		if (data instanceof Collection && ((Collection<?>) data).isEmpty()) {
			return true;
		}
		if (data instanceof Map && ((Map<?, ?>) data).isEmpty()) {
			return true;
		}
		if (data instanceof Object[] && ((Object[]) data).length == 0) {
			return true;
		}
		return false;
	}

	/*조건문에 따라 예외 발생*/
	public static void throwRestCustomExceptionIf(boolean conditionalStatement, ErrorCode errorCode) {
		if(conditionalStatement) {
			throw new RestCustomException(errorCode);
		}
	}
}
