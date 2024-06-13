package com.planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.planner.oauth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {// 시큐리티를 적용하지 않을 리소스
		return web -> web.ignoring()
				.requestMatchers("/error");
	}
	
	@Bean // 빈객체주입
	// 필터 체인을 정의하는 메서드
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
	      http.csrf(AbstractHttpConfigurer::disable)
          .formLogin(AbstractHttpConfigurer::disable)
          .httpBasic(AbstractHttpConfigurer::disable)
          .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // For H2 DB
          .authorizeHttpRequests((requests) -> requests
                  .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                  .anyRequest().authenticated()
          )
          .oauth2Login((configure -> configure.userInfoEndpoint(c -> c.userService(oAuth2UserService))))
				.logout((logout)->logout							//사용자 정의
							.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 URL 과
							.logoutSuccessUrl("/question/list")		// 성공시 리다이렉트 URL 설정
							.invalidateHttpSession(true))		;		// 세션 삭제
		return http.build();
	}
}
