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
import com.planner.oauth.handler.CustomAccessDeniedHandler;
import com.planner.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.planner.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.planner.oauth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;	// 소셜로그인 사용자 정의 객체 생성을 위한 클래스
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;		// 소셜로그인 성공 시 작동하는 클래스(핸들러)
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;		// 소셜로그인 실패 시 작동하는 클래스(핸들러)
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;	// redirectURl, state, mode 등 담아놓는 쿠키 생성하는 클래스

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {// 시큐리티를 적용하지 않을 리소스
		return web -> web.ignoring()
				.requestMatchers("/error/**","/css/**", "/js/**", "/images/**");// 정적 리소스 시큐리티 무시 => 안하면 적용이 안됌
	}
	
	// 필터 체인을 정의하는 메서드
	@Bean // 빈객체주입
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
	      http.csrf((csrf)->csrf
					.ignoringRequestMatchers(new AntPathRequestMatcher
							("/n")))												// 특정요청에대한 보호를 비활성화
          .httpBasic(AbstractHttpConfigurer::disable)			// http 기본인증 비활성화
          
          .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // For H2 DB 기능 비활성화
          .authorizeHttpRequests((requests) -> requests
        		  .requestMatchers(new AntPathRequestMatcher("/intro/**")).permitAll()					// "/planner/main" 은 모든권한의 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/planner/main")).permitAll()				// "/planner/main" 은 모든권한의 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/member/anon/**")).permitAll() 			// "/member/anon/**" 은 모든권한의 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()						// "/oauth2/**" 은 모든 권한의 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/planner/**")).hasAnyRole("USER","SUPER_ADMIN")					// "/planner/main" 은 모든권한의 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/friend/**")).hasAnyRole("USER","SUPER_ADMIN")			// "/oauth2/**" 은 모든 권한의 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/member/auth/**")).hasAnyRole("USER","SUPER_ADMIN")	// "/member/auth/**" 은 USER 권한을 가진 사용자만 접속을 허용함
                  .requestMatchers(new AntPathRequestMatcher("/team/**")).hasAnyRole("USER","SUPER_ADMIN")
                  .requestMatchers(new AntPathRequestMatcher("/vote/**")).hasAnyRole("USER","SUPER_ADMIN")	
                  .requestMatchers(new AntPathRequestMatcher("/reply/**")).hasAnyRole("USER","SUPER_ADMIN")	
                  .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("SUPER_ADMIN")
                  .anyRequest().authenticated()
          )
          .oauth2Login(configure ->															// OAuth2 인증 로그인( 소셜 ) 정의
          configure.loginPage("/member/anon/login")								// 스프링 소셜로그인 페이지가 아닌 사용자 정의 로그인 페이지 지정
          		  .authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))// OAuth2 인증 엔드포인트 설정
                  .userInfoEndpoint(config -> config.userService(customOAuth2UserService)) /// OAuth2 사용자 정보 엔드포인트 설정
                  .successHandler(oAuth2AuthenticationSuccessHandler)	// OAuth2 로그인 성공 핸들러 설정
                  .failureHandler(oAuth2AuthenticationFailureHandler)		// OAuth2 로그인 실패 핸들러 설정
  )	
          .formLogin((formLogin) -> formLogin											// 폼(일반) 로그인 정의
					.loginPage("/member/anon/login")									// 로그인페이지 설정
					.usernameParameter("member_email")							// 시큐리티 Username 사용자정의
					.passwordParameter("member_password") 						//시큐리티 password 사용자정의
					.failureUrl("/member/anon/fail")									// 로그인 실패시 URL
					.defaultSuccessUrl("/member/auth",true))						// 로그인 성공시 URL 	
          			
          
          .logout((logout)->logout																							// 로그아웃 정의
					.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) 	// 로그아웃 URL 
					.logoutSuccessUrl("/planner/main")																// 로그아웃 성공시 리다이렉트 URL 
					.invalidateHttpSession(true))
          .exceptionHandling((exception)-> exception.accessDeniedHandler(customAccessDeniedHandler))
          ;																		// 세션 삭제
		
		return http.build();
	}

}
