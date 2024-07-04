package com.planner.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planner.dto.request.schedule.MapLikeDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.MapLikeService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MapLikeController {

	private final MapLikeService mapLikeService;

	@PostMapping("list")
	public String list(RedirectAttributes redirect, MapLikeDTO dto, @UserData ResMemberDetail detail,
			@RequestParam("map_title") String map_title, @RequestParam("map_address") String map_address) {
		dto.setMember_id(detail.getMember_id());
		List<MapLikeDTO> mapLikeList = mapLikeService.MapLikeSelect(detail.getMember_id());
		for (MapLikeDTO MapLikedto1 : mapLikeList) {
			if (MapLikedto1.getMap_address().equals(map_address)) {
				redirect.addFlashAttribute("msg", "이미 즐겨찾기된 주소입니다!");
				return "redirect:/planner/calendar";
			}
		}
		mapLikeService.MapLikeInsert(dto);
		redirect.addFlashAttribute("msg", "즐겨찾기 추가 되었습니다!");
		return "redirect:/planner/calendar";
	}

	@GetMapping("mapLikeList")
	public String mapLikeList( Model model, MapLikeDTO MapLikedto,@UserData ResMemberDetail detail) {
		MapLikedto.setMember_id(detail.getMember_id());
		List<MapLikeDTO> mapLikeList = mapLikeService.MapLikeSelect(detail.getMember_id());
		model.addAttribute("mapLikeList", mapLikeList);
		for (MapLikeDTO MapLikedto1 : mapLikeList) {
		}
		return "mapLikeList";
	}

	@PostMapping("mapLikeDelete")
	public String mapLikeDelete(RedirectAttributes redirect, @UserData ResMemberDetail detail,MapLikeDTO MapLikedto,
			@RequestParam("mapLikeListcheckbox") List<Integer> map_numbers, Model model) {
		MapLikedto.setMember_id(detail.getMember_id());
		mapLikeService.MapLikeDelete(detail.getMember_id(), map_numbers);
		redirect.addFlashAttribute("msg", "삭제 되었습니다");
		return "redirect:/planner/calendar";
	}
}
