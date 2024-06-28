package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.team.TeamDTO;

@Mapper
@Repository
public interface TeamMapper {
	public int teamNameOverlap(String team_name);
	public void teamInsert(TeamDTO dto);
	public void teamUpdate(TeamDTO dto);
	public int teamDelete(@Param("team_id")Long team_id,@Param("member_id") Long member_id);
	public TeamDTO teamInfo(Long team_id);
}
