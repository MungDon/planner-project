package com.planner.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.enums.CodeStatus;
import com.planner.exception.ErrorCode;
import com.planner.exception.RestCustomException;
import com.planner.mapper.EmailMapper;
import com.planner.util.CommonUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
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
	public void sendAuthCode(String toEmail) throws MessagingException, UnsupportedEncodingException {
		String authCode = createAuthCode(toEmail);
		MimeMessage message = createEmail(toEmail, authCode);
		javaMailSender.send(message);
	}

	//현재 이메일로 인증받았던 이전 인증코드들 삭제
	// 해당이메일의 기존 인증기록이 남아있으면 이전 코드도 인증이 되어버리기때문에 인증코드 생성전에 삭제
	@Transactional
	private void deletePrevEmailAuthCode(String toEmail) {
		int isExists = emailMapper.isExists(toEmail);
		if(isExists >=1) {
			emailMapper.deleteEmailAuthCode(toEmail);
		}
	}
	
	// 이메일 인증 코드 생성
	@Transactional
	private String createAuthCode(String toEmail) {
		StringBuilder authCode = new StringBuilder();
		Random ramdom = new Random();
		for(int i  = 0; i<7; i++) {
			authCode.append(ramdom.nextInt(10)); //각자리마다  0~9 까지 랜덤숫자로 7자리 생성
		}
		deletePrevEmailAuthCode(toEmail); 				// 이전 기록삭제
		
		int result = emailMapper.saveAuthCode(toEmail, authCode.toString(),CodeStatus.CODE_UNCHECKED.getStatus()); // 인증코드저장
		CommonUtils.throwRestCustomExceptionIf(result !=1,  ErrorCode.FAIL_CREATE_AUTHCODE);
		return authCode.toString();
	}

	// 보낼 이메일 생성
	private MimeMessage createEmail(String toEmail, String authCode) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		String msg = "";
		msg += "<div><h1>Plandas  인증 코드입니다</h1>";
		msg += "<h2>아래의 인증코드를 페이지에 입력해주세요</h2>";
		msg += "<h3>인증코드 : " + authCode + "</h3></div>";
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
		message.setFrom(new InternetAddress(senderEmail, "PLANDAS 운영자"));
		message.setSubject("Plandas 인증코드입니다.");
		message.setText(msg, "UTF-8", "html");
		return message;
	}
	 
	//인증 번호 검증
	@Transactional
	public void authCodeChk(String toEmail, String authCode) {
		int result = emailMapper.authCodeChk(toEmail, authCode);
		CommonUtils.throwRestCustomExceptionIf(result !=1,  ErrorCode.FAIL_AUTHENTICATION);
		emailMapper.updateCodeChk(toEmail,CodeStatus.CODE_CHECKED.getStatus());
	}

	// 스케쥴러로 정기적으로 잉여데이터 전부다 삭제
	@Transactional
	public void removeAllEmailAuthCode() {
		emailMapper.removeAll();
	}
}
