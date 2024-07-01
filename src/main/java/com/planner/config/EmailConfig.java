package com.planner.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	@Value("${spring.mail.host}")
	private String host;                        			// 이메일 서버의 SMTP 호스트 

	@Value("${spring.mail.port}")
	private int port;                           			// SMTP 서버의 포트 번호 

	@Value("${spring.mail.username}")
	private String username;                   	 	// SMTP 서버에 등록된 사용자 이름 (이메일 주소)

	@Value("${spring.mail.password}")
	private String password;                    		// SMTP 서버용 앱 전용 비밀번호

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean auth;                      	 	// SMTP 인증 사용 여부 (true 또는 false)

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean starttlsEnable;             	// STARTTLS 활성화 여부 (true 또는 false)

	@Value("${spring.mail.properties.mail.smtp.starttls.required}")
	private boolean starttlsRequired;           // STARTTLS 필수 여부 (true 또는 false)

	@Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
	private int connectionTimeout;              // 연결 타임아웃 (밀리초 단위)

	@Value("${spring.mail.properties.mail.smtp.timeout}")
	private int timeout;                        			// SMTP 서버 응답 대기 타임아웃 (밀리초 단위)

	@Value("${spring.mail.properties.mail.smtp.writetimeout}")
	private int writeTimeout;                   		// SMTP 서버 쓰기 타임아웃 (밀리초 단위)

	
	// 메일 보내는 객체 JavaMailSender 를 스프링 Bean 으로등록
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(getMailProperties());
		
		return mailSender;
	}
	
	// 메일보낼때 설정되는 properties
	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth",auth);
		properties.put("mail.smtp.starttls.enable",starttlsEnable);
		properties.put("mail.smtp.starttls.required",starttlsRequired);
		properties.put("mail.smtp.connectiontimeout",connectionTimeout);
		properties.put("mail.smtp.timeout",timeout);
		properties.put("mail.smtp.writetimeout",writeTimeout);
		
		return properties;
	}
}
