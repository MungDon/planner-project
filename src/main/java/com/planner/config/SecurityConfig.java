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

import com.planner.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.planner.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.planner.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.planner.oauth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {// 시큐리티를 적용하지 않을 리소스
		return web -> web.ignoring()
				.requestMatchers("/error","/css/**", "/js/**", "/images/**");// 정적 리소스 시큐리티 무시 => 안하면 적용이 안됌
	}
	
	@Bean // 빈객체주입
	// 필터 체인을 정의하는 메서드
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
	      http.csrf((csrf)->csrf
					.ignoringRequestMatchers(new AntPathRequestMatcher
							("/planner")))												// 특정요청에대한 보호를 비활성화
          .httpBasic(AbstractHttpConfigurer::disable)
          
          .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // For H2 DB
          .authorizeHttpRequests((requests) -> requests
                  .requestMatchers(new AntPathRequestMatcher("/planner/main")).permitAll()
                  .requestMatchers(new AntPathRequestMatcher("/member/anon/**")).permitAll()
                  .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
                  .requestMatchers(new AntPathRequestMatcher("/member/auth/**")).hasRole("USER")
                  .anyRequest().authenticated()
          )
          .oauth2Login(configure ->
          configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                  .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                  .successHandler(oAuth2AuthenticationSuccessHandler)
                  .failureHandler(oAuth2AuthenticationFailureHandler)
  )	
          .formLogin((formLogin) -> formLogin		// 사용자 정의
					.loginPage("/member/login")		// 로그인페이지 설정과
					.usernameParameter("member_email")
					.passwordParameter("member_password") 		//default = password, username
					.failureUrl("/member/fail")
					.defaultSuccessUrl("/member",true))	// 리다이렉트 URL 설정	
          			
          
          .logout((logout)->logout							//사용자 정의
					.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 URL 과
					.logoutSuccessUrl("/planner/main")		// 성공시 리다이렉트 URL 설정
					.invalidateHttpSession(true))	;			// 세션 삭제
			
		return http.build();
	}

}
