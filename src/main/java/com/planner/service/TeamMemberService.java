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

	// 그룹에서 해당 인원의 tm_grade 검색
	public String teamMemberGrade(long team_id, long member_id) {
		return tmMapper.teamMemberGrade(team_id, member_id);
	}

	// 그룹 내에서 별명 중복 확인
	public int nickCheck(Long team_id, String tm_nickname) {
		return tmMapper.nickCheck(team_id, tm_nickname);
	}

	// 그룹 가입 신청
	public int tmInsert(Long team_id, Long member_id, String tm_nickname){
		TeamMemberDTO dto = TeamMemberDTO.builder()
								.team_id(team_id)
								.member_id(member_id)
								.tm_nickname(tm_nickname)
								.tm_grade(TM_Grade.ROLE_WAIT.getValue())
								.build();
		try {
			return tmMapper.insertTeamMember(dto);
		}catch(Exception e) {
			return -1;
		}
		// 무결성 제약 오류 => OracleDatabaseException
	}

	// 그룹 인원 목록
	public List<TeamMemberDTO> tmInfoList(Long team_id) {
		return tmMapper.tmInfoList(team_id);
	}

	// 그룹 인원 제거 (추방 삭제 둘다 사용함)
	public int tmDelete(Long team_id, Long member_id) {
		return tmMapper.tmDelete(team_id, member_id);
	}

	// 그룹원 정보 (member_id)
	public TeamMyInfoDTO myinfo(Long team_id, Long member_id) {
		return tmMapper.myinfo(team_id, member_id);
	}

	// 그룹원 정보(team_id)
	public TeamMyInfoDTO myinfo2(Long team_id, Long team_member_id) {
		return tmMapper.myinfo2(team_id, team_member_id);
	}

	// 그룹 내 별명 수정
	public int tmUpdate(Long team_id, Long member_id, String tm_nickname) {
		try {
			return tmMapper.tmUpdate(team_id, member_id, tm_nickname);
		} catch (Exception e) {
			return -1;
		}
	}

	// 그룹 가입 승인 (tm_grade "WAIT" -> "TEAM_USER"로 변경)
	public int accept(Long team_id, Long member_id, String tm_grade) {
		return tmMapper.accept(team_id, member_id, tm_grade);
	}

	// tm_grade 변경 (그룹장 권한)
	public int gradeModify(Long team_id, Long member_id, String tm_grade) {
		return tmMapper.gradeModify(team_id, member_id, tm_grade);
	}

	// team_member_id 검색
	public long teamMemberId(long team_id, long member_id) {
		return tmMapper.myTeamMemberId(team_id, member_id);
	}

}
