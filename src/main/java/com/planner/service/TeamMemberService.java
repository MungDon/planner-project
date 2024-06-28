package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.team.TeamMemberDTO;
import com.planner.dto.request.team.TeamMyInfoDTO;
import com.planner.enums.TM_Grade;
import com.planner.mapper.TeamMemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

	private final TeamMemberMapper tmMapper;

	public String teamMemberOverlap(long team_id, long member_id) {
		return tmMapper.teamMemberOverlap(team_id, member_id);
	}
	
	public void tmInsert(Long team_id, Long member_id, String nickname) {
		TeamMemberDTO dto = new TeamMemberDTO();
		dto.setTeam_id(team_id);
		dto.setMember_id(member_id);
		dto.setTm_nickname(nickname);
		dto.setTm_grade(TM_Grade.ROLE_TEAM_WAIT.getValue());
		tmMapper.insertTeamMember(dto);
	}

	public List<TeamMemberDTO> tmInfoList(Long team_id) {
		return tmMapper.tmInfoList(team_id);
	}

	public int tmDelete(Long team_id, Long member_id) {
		return tmMapper.tmDelete(team_id, member_id);
	}

	public TeamMyInfoDTO myinfo(Long team_id, Long member_id) {
		return tmMapper.myinfo(team_id, member_id);
	}

	public void tmUpdate(Long team_id, Long member_id, String tm_nickname) {
		tmMapper.tmUpdate(team_id, member_id, tm_nickname);
	}

	public int accept(Long team_id, Long member_id, String tm_grade) {
		return tmMapper.accept(team_id, member_id, tm_grade);
	}
	
	public int gradeModify(Long team_id, Long member_id, String tm_grade) {
		return tmMapper.gradeModify(team_id, member_id, tm_grade);
	}

}
