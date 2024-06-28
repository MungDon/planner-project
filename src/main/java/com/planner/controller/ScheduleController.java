package com.planner.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.planner.dto.request.schedule.MapDTO;
import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/planner/")
@Controller
@RequiredArgsConstructor
public class ScheduleController {

	private final ScheduleService scheduleService;
	
	/*
	@GetMapping("/cal")
	public String cal() {
		return "gptcal";
	}
	*/
	
	@GetMapping("calendar")
	public String calendar() {
		return "calendar";
	}
	
	
	
	@GetMapping("schedule")
	public String right(ScheduleDTO scheduleDTO, Model model, @RequestParam("date") String date) {
		List<ScheduleDTO> list = null;
//		if(date== null) {
//			return "redirect:/calendar";	// 잘못된 접근임 지금 기능 없음 깡통임
//		}
		list = scheduleService.schedule_select(date);
		model.addAttribute("date", date);
		// @RequestParam("date") String date
		model.addAttribute("list", list);
		
		return "schedule";
		
	}
	
	// 글 썼을때
	@PostMapping("schedule")
	public String schedulePro(ScheduleDTO scheduleDTO, @RequestParam("date") String date, Model model ,
											 @RequestParam("place") String place , @RequestParam("address") String address, MapDTO mapDTO) {
		model.addAttribute("place", place);
		model.addAttribute("address", address);
		model.addAttribute("date", date);
		scheduleService.schedule_insert(scheduleDTO,mapDTO);
		return "redirect:/planner/calendar";
		//return String.format("redirect:/right?date=%s",date);
	}
	
	// 글삭제
	@GetMapping("schedule/del")
	public String scheduleDel(@RequestParam("schedule_id") Long schedule_id) {
//		@PathVariable("schedule_id") int schedule_id
		scheduleService.schedule_delete(schedule_id);
		
		return "redirect:/planner/calendar";
	}
	
	// 글수정
	@PostMapping("schedule/edt")
	public String schedulePro(ScheduleDTO scheduleDTO,  Model model) {
		//, @RequestParam("schedule_id") int schedule_id
		scheduleService.schedule_update(scheduleDTO);
		return "redirect:/planner/calendar";
		//return String.format("redirect:/right?date=%s",date);
	}
	
	
	
}
