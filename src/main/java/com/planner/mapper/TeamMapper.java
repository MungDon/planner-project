package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.team.MyTeamListDTO;
import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.request.team.TeamInfoDTO;

@Mapper
@Repository
public interface TeamMapper {
	public int teamNameOverlap(String team_name);
	public void teamInsert(TeamDTO dto);
	public void teamUpdate(TeamDTO dto);
	public int teamDelete(@Param("member_id") long member_id, @Param("team_id")long team_id);
	public TeamDTO teamInfo(long team_id);
	public TeamInfoDTO teamAndMasterInfo(long team_id);
	public List<MyTeamListDTO> myTeamList(Long member_id);
	public List<TeamInfoDTO> teamListSearch(@Param("start")long start, @Param("end")long end,
										@Param("searchOption")String searchOption,
										@Param("search")String search);
	public long teamCount(@Param("searchOption")String searchOption, @Param("search")String search);
}
