package com.planner.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.planner.util.UserDataResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
	private final UserDataResolver userDataResolver;

	// 커스텀 어노테이션 리졸버 등록 / 이걸안하면 커스텀 어노테이션이 동작을 안함
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userDataResolver);
	}
	
}
