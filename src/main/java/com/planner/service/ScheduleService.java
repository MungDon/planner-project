package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.schedule.MapDTO;
import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.mapper.MapRepository;
import com.planner.mapper.ScheduleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleMapper scheduleMapper;
	private final MapRepository mapRepository;
	
	public void schedule_insert(ScheduleDTO scheduleDTO , MapDTO mapDTO) {
		scheduleMapper.schedule_insert(scheduleDTO);
		mapDTO.setSchedule_id(scheduleDTO.getSchedule_id());
		mapRepository.MapInsert(mapDTO);
	}
	
	public List<ScheduleDTO> schedule_select(String date) {
		return scheduleMapper.schedule_select(date);
	}
	
	public int schedule_update(ScheduleDTO scheduleDTO) {
		return scheduleMapper.schedule_update(scheduleDTO);
	}
	
	public int schedule_delete(Long schedule_id) {
		mapRepository.MapDelete(schedule_id);
		return scheduleMapper.schedule_delete(schedule_id);
	}
	
	
	
}

