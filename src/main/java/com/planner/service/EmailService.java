package com.planner.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.mapper.EmailMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final EmailMapper emailMapper;

	@Value("${spring.mail.username}")
	private String senderEmail;

	// 이메일 인증 코드 보내기
	@Transactional
	public void sendAuthCode(String toEmail) throws MessagingException {
		String authCode = createAuthCode(toEmail);
		MimeMessage message = createEmail(toEmail, authCode);
		javaMailSender.send(message);
	}

	// 이메일 인증 코드 생성
	private String createAuthCode(String toEmail) {
		StringBuilder authCode = new StringBuilder();
		Random ramdom = new Random();
		for(int i  = 0; i<7; i++) {
			authCode.append(ramdom.nextInt(10)); // 0~9 까지 랜덤숫자
		}
		int result = emailMapper.saveAuthCode(toEmail, authCode.toString());
		if(result != 1) {
			throw new CustomException(ErrorCode.FAIL_CREATE_AUTHCODE);
		}
		return authCode.toString();
	}

	// 보낼 이메일 생성
	private MimeMessage createEmail(String toEmail, String authCode) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		String msg = "";
		msg += "<div><h1>Planner 회원가입 인증 코드입니다</h1>";
		msg += "<h2>아래의 인증코드를 페이지에 입력해주세요</h2>";
		msg += "<span>인증코드 : " + authCode + "</span></div>";
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
		message.setFrom(senderEmail);
		message.setSubject("Planner 회원가입 인증코드입니다.");
		message.setText(msg, "UTF-8", "html");
		return message;
	}
	
	/*인증 번호 검증*/
	@Transactional(readOnly = true)
	public int authCodeChk(String toEmail, String authCode) {
		int result = 0;
		result = emailMapper.authCodeChk(toEmail, authCode);
		return result;
	}

}
