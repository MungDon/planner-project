package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.team.TeamMemberDTO;
import com.planner.dto.request.team.TeamMyInfoDTO;

@Mapper
@Repository
public interface TeamMemberMapper {
	public String teamMemberOverlap(@Param("team_id")long team_id, @Param("member_id")long member_id);
	
	public void insertTeamMember(TeamMemberDTO dto);

	public TeamMyInfoDTO myinfo(@Param("team_id")Long team_id, @Param("member_id")Long member_id);
	
	public List<TeamMemberDTO> tmInfoList(long team_id);

	public void tmUpdate(@Param("team_id")long team_id, @Param("member_id")long member_id,
			@Param("tm_nickname") String tm_nickname);
	
	public int tmDelete(@Param("team_id")long team_id, @Param("member_id")long member_id);


	public int accept(@Param("team_id")Long team_id, @Param("member_id")Long member_id, 
			@Param("tm_grade")String tm_grade);
	
	public int gradeModify(@Param("team_id")Long team_id, @Param("member_id")Long member_id, 
							@Param("tm_grade")String tm_grade);

}
