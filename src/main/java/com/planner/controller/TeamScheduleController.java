package com.planner.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.schedule.MapDTO;
import com.planner.dto.request.schedule.MapLikeDTO;
import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.dto.request.schedule.ScheduleSearchDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.TM_Grade;
import com.planner.service.MapLikeService;
import com.planner.service.ScheduleService;
import com.planner.service.TeamMemberService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/team/planner/")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class TeamScheduleController {

	private final ScheduleService scheduleService;
	private final MapLikeService mapLikeService;
	private final TeamMemberService tmService;

	// 달력 페이지
	@PreAuthorize("isAuthenticated()")
	@GetMapping("calendar")
	public String calendar(@UserData ResMemberDetail detail, Model model,
			@RequestParam(name = "team_id", defaultValue = "-1") Long team_id) {
		// 등급 갖고와서 대기중 or 미가입일시 보내버림
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if (tm_grade == null || TM_Grade.ROLE_WAIT.getValue().equals(tm_grade)) {
			return "redirect:/team/main";
		}
		model.addAttribute("tm_grade", tm_grade);
		model.addAttribute("team_id", team_id);
		return "calendar";
	}

	// list
	@GetMapping("schedule")
	public String right(ScheduleDTO scheduleDTO, Model model, MapLikeDTO MapLikedto, @RequestParam("date") String date,
			@UserData ResMemberDetail detail, @RequestParam(name = "team_id", defaultValue = "-1") Long team_id,
			@ModelAttribute("tm_grade")String tm_grade) {
		List<ScheduleDTO> list = null;
		list = scheduleService.schedule_select(detail.getMember_id(), date, team_id);
		MapLikedto.setMember_id(detail.getMember_id());
		ArrayList<MapLikeDTO> mapLikeList = mapLikeService.MapLikeSelect(detail.getMember_id());
		model.addAttribute("mapLikeList", mapLikeList);
		model.addAttribute("date", date);
		model.addAttribute("list", list);
		return "schedule";
	}

	// 글 쓰기
	@PostMapping("schedule")
	public String schedulePro(ScheduleDTO scheduleDTO, @RequestParam("date") String date, Model model,
			@UserData ResMemberDetail detail, MapDTO mapDTO) {
		model.addAttribute("date", date);
		scheduleService.schedule_insert(scheduleDTO, mapDTO, detail);
		return "redirect:/planner/calendar";
	}

	// 글삭제
	@ResponseBody
	@DeleteMapping("schedule/del")
	public void scheduleDel(@RequestParam("schedule_id") Long schedule_id) {
		scheduleService.schedule_delete(schedule_id);
	}

	// 글수정
	@PostMapping("schedule/edt")
	public String schedulePro(ScheduleDTO scheduleDTO, MapDTO mapDTO, Model model) {
		scheduleService.schedule_update(scheduleDTO, mapDTO);
		return "redirect:/planner/calendar";
	}

	@GetMapping("search")
	public String scheduleSearch(ScheduleSearchDTO dto, Model model) {
		List<ScheduleDTO> list = scheduleService.schedule_search(dto);
		model.addAttribute("list", list);
		return "/team/board/scSearch";
	}
	
}
