package com.planner.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.planner.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {
	
	private final EmailService emailService;
	
	@Scheduled(cron = "0 0 0 * * *")// 매일 00시에 인증코드 DB 정보 초기화(잉여데이터 제거)
	public void removeAllEmailAuthCode() {
		log.info("스케쥴러발동");
		emailService.removeAllEmailAuthCode();
	}
}
