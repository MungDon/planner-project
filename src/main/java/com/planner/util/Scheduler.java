package com.planner.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.planner.service.EmailService;
import com.planner.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
	
	private final EmailService emailService;
	private final NoticeService noticeService;
	
	@Scheduled(cron = "0 0 0 * * *")// 매일 00시에 인증코드 DB 정보 초기화(잉여데이터 제거)
	public void removeAllEmailAuthCode() {
		log.info("이메일스케쥴러발동");
		emailService.removeAllEmailAuthCode();
	}
	@Scheduled(cron = "0 0 0 * * *")// 매일 00시에 인증코드 DB 정보 초기화(잉여데이터 제거)
	public void deleteImagesWithoutNoticeId() {
		log.info("이미지삭제스케쥴러발동");
		noticeService.deleteImagesWithoutNoticeId();
	}
}
