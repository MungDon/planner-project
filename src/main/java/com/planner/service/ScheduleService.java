package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.schedule.MapDTO;
import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.mapper.MapRepository;
import com.planner.mapper.ScheduleMapper;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleMapper scheduleMapper;
	private final MapRepository mapRepository;
	
	public void schedule_insert(ScheduleDTO scheduleDTO , MapDTO mapDTO, @UserData ResMemberDetail detail ) {
		scheduleDTO.setMember_id(detail.getMember_id());
		scheduleMapper.schedule_insert(scheduleDTO);
		mapDTO.setSchedule_id(scheduleDTO.getSchedule_id());
		mapRepository.MapInsert(mapDTO);
	}
	
	public List<ScheduleDTO> schedule_select(Long member_id, String date) {
		return scheduleMapper.schedule_select(member_id, date);
	}
	
	public void schedule_update(ScheduleDTO scheduleDTO, MapDTO mapDTO) {
		mapRepository.MapUpdate(mapDTO);
		 scheduleMapper.schedule_update(scheduleDTO);
	}
	
	public int schedule_delete(Long schedule_id) {
		mapRepository.MapDelete(schedule_id);
		return scheduleMapper.schedule_delete(schedule_id);
	}
	
	
	
}

