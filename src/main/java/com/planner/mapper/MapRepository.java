package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.schedule.MapDTO;



@Repository
@Mapper
public interface MapRepository {
	public int MapInsert(MapDTO dto);
	
	public int MapList(MapDTO dto);
	
	public int MapDelete(Long schedule_id);
	
	public int MapUpdate(MapDTO dto);
	
}
