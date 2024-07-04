package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.schedule.ScheduleDTO;


@Repository
@Mapper
public interface ScheduleMapper {

	public void schedule_insert(ScheduleDTO scheduleDTO);
	
	public List<ScheduleDTO> schedule_select(@Param("member_id")Long member_id,@Param("date") String date);
	
	public int schedule_update(ScheduleDTO scheduleDTO);
	
	public int schedule_delete(Long schedule_id);
	
	
	
	
	
	
}
