package com.planner.service;

import org.springframework.stereotype.Service;

import com.planner.dto.request.schedule.MapDTO;
import com.planner.mapper.MapRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapService {
	private final MapRepository mapRepository;
	
	public void mapInsert(MapDTO dto) {
		mapRepository.MapInsert(dto);
	}	
	public void mapList(MapDTO dto) {
		mapRepository.MapList(dto);
	}
}
