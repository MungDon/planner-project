package com.planner.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.schedule.MapLikeDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.MapLikeService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MapLikeController {

	private final MapLikeService mapLikeService;

	// 즐겨찾기 추가 
	@PostMapping("list")
	@ResponseBody
	public ResponseEntity<Long> list(MapLikeDTO dto, @UserData ResMemberDetail detail) {
		dto.setMember_id(detail.getMember_id());
		mapLikeService.mapLikeDuplicate(dto);
		mapLikeService.MapLikeInsert(dto);
		Long map_number = dto.getMap_number();
		return ResponseEntity.ok(map_number);
	}
	// 비동기 즐겨찾기 보기
	@GetMapping("updateList")
	@ResponseBody
	public ResponseEntity<List<MapLikeDTO>> updateList(@UserData ResMemberDetail detail,MapLikeDTO dto){
		List<MapLikeDTO> mapLikeList =mapLikeService.MapLikeSelect(detail.getMember_id());
		return ResponseEntity.ok(mapLikeList);
	}
	// 즐겨찾기 삭제
	@PostMapping("mapLikeDelete")
	@ResponseBody
	public ResponseEntity<String> mapLikeDelete( @UserData ResMemberDetail detail,MapLikeDTO MapLikedto,
			@RequestParam("mapLikeListcheckbox") List<Integer> map_numbers, Model model) {
		MapLikedto.setMember_id(detail.getMember_id());
		mapLikeService.MapLikeDelete(detail.getMember_id(), map_numbers);
		return ResponseEntity.ok("ok");
	}
}

