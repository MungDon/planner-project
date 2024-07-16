package com.planner.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.admin.NoticeDTO;
import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.dto.request.schedule.TodayInfo;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
import com.planner.service.NoticeService;
import com.planner.service.ScheduleService;
import com.planner.util.CommonUtils;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final ScheduleService scheduleService;
	private final NoticeService noticeService;

	private final static Long NO_TEAM = -1L;
	
	
	@GetMapping("/intro")
	public String intro() {
		return "intro";
	}

	@GetMapping("/planner/main")
	public String main(@UserData ResMemberDetail detail, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (!CommonUtils.isEmpty(detail)) {
			if (detail.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				return "redirect:/oauth2/auth/signup";
			}
			TodayInfo todayInfo = CommonUtils.getTodayInfo();
			List<ScheduleDTO> todaySchedule = scheduleService.schedule_select(detail.getMember_id(), todayInfo.getCurrentDate(), NO_TEAM);
			List<NoticeDTO> notice = noticeService.noticeSelect(1,10);
			model.addAttribute("notice", notice);
			model.addAttribute("todayInfo", todayInfo);
			model.addAttribute("todaySchedule", todaySchedule);
			model.addAttribute("member", detail);
			return "main";
		}
		return "redirect:/member/anon/login";
	}

	@GetMapping("/notice/detail/{notice_id}")
	@ResponseBody
	public ResponseEntity<NoticeDTO> noticeContent(@PathVariable("notice_id") Long notice_id,Model model) {
		NoticeDTO noticeDTO = noticeService.noticeContent(notice_id);
		model.addAttribute("noticeDTO", noticeDTO);
		return ResponseEntity.ok(noticeDTO);
	}
}
