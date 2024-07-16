package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.schedule.CalendarDTO;
import com.planner.dto.request.schedule.CalendarPrintDTO;
import com.planner.dto.request.schedule.MapDTO;
import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.dto.request.schedule.ScheduleSearchDTO;
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

	public void schedule_insert(ScheduleDTO scheduleDTO, MapDTO mapDTO, @UserData ResMemberDetail detail) {
		scheduleDTO.setMember_id(detail.getMember_id());
		scheduleMapper.schedule_insert(scheduleDTO);
		mapDTO.setSchedule_id(scheduleDTO.getSchedule_id());
		mapRepository.MapInsert(mapDTO);
	}

	public List<ScheduleDTO> schedule_select(Long member_id, String date, Long team_id) {
		return scheduleMapper.schedule_select(member_id, date, team_id);
	}

	public void schedule_update(ScheduleDTO scheduleDTO, MapDTO mapDTO) {
		mapRepository.MapUpdate(mapDTO);
		scheduleMapper.schedule_update(scheduleDTO);
	}

	public int schedule_delete(Long schedule_id) {
		mapRepository.MapDelete(schedule_id);
		return scheduleMapper.schedule_delete(schedule_id);
	}

	public List<ScheduleDTO> schedule_search(ScheduleSearchDTO dto) {
		return scheduleMapper.schedule_search(dto);
	};

	public ScheduleDTO schedule_select_one(Long schedule_id) {
		return scheduleMapper.schedule_select_one(schedule_id);
	}

	public List<CalendarPrintDTO> calendarPrint(CalendarDTO dto) {
		return scheduleMapper.calendarPrint(dto);
	}

}
