package com.planner.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
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
			LocalDate today = LocalDate.now();
			 DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
			String todayProvide = today.format(todayFormat);
			List<ScheduleDTO> todaySchedule = scheduleService.schedule_select(detail.getMember_id(), todayProvide, NO_TEAM);
			model.addAttribute("todaySchedule", todaySchedule);
			model.addAttribute("member", detail);
			return "main";
		}
		return "redirect:/member/anon/login";
	}

}
