package com.planner.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.schedule.MapLikeDTO;
import com.planner.exception.ErrorCode;
import com.planner.mapper.MapLikeMapper;
import com.planner.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapLikeService {
	private final MapLikeMapper mapLikeMapper;
	
	public void MapLikeInsert(MapLikeDTO dto) {
		mapLikeMapper.MapLikeInsert(dto);
	}
	public ArrayList<MapLikeDTO> MapLikeSelect(Long member_id) {
		return mapLikeMapper.MapLikeSelect(member_id);
	}
	public List<MapLikeDTO> MapLikeAllSelect(Long member_id,Long map_number) {
		return mapLikeMapper.MapLikeAllSelect(member_id, map_number);
	}
	public int MapLikeDelete(Long member_num,List<Integer> map_numbers) {
		return mapLikeMapper.MapLikeDelete(member_num,map_numbers);
	}
	public void mapLikeDuplicate(MapLikeDTO dto) {
		int result = mapLikeMapper.mapLikeDuplicate(dto);
		CommonUtils.throwRestCustomExceptionIf(result != 0, ErrorCode.PLACE_FAVORITES_DUPLICATE);
	}
}

