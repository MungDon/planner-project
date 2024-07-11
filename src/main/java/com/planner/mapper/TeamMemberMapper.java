package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.team.TeamMemberDTO;
import com.planner.dto.request.team.TeamMyInfoDTO;

// team_id + tm_nickname 두개 조합이 unique라서
// 무결성 제약에 의한 오류 발생 가능성이 생김. exception 처리가 필요함.
@Mapper
@Repository
public interface TeamMemberMapper {

	// 해당 그룹에서 특정 회원의 tm_grade 검색
	public String teamMemberGrade(@Param("team_id")long team_id, @Param("member_id")long member_id);

	// 그룹 내에서 별명 중복 확인
	public int nickCheck(@Param("team_id")Long team_id, @Param("tm_nickname")String tm_nickname);

	// 그룹 가입 신청
	public int insertTeamMember(TeamMemberDTO dto) throws Exception;

	// 그룹원 정보 (member_id)
	public TeamMyInfoDTO myinfo(@Param("team_id")Long team_id, @Param("member_id")Long member_id);

	// 그룹원 정보 (team_member_id)
	public TeamMyInfoDTO myinfo2(@Param("team_id")Long team_id, @Param("team_member_id")Long team_member_id);

	// 그룹원 목록
	public List<TeamMemberDTO> tmInfoList(long team_id);

	// 별명 수정
	public int tmUpdate(@Param("team_id")long team_id, @Param("member_id")long member_id,
			@Param("tm_nickname") String tm_nickname) throws Exception;

	// 그룹원 제거(추방, 탈퇴)
	public int tmDelete(@Param("team_id")long team_id, @Param("member_id")long member_id);

	// 가입 승인 (WAIT -> TEAM_USER)
	public int accept(@Param("team_id")Long team_id, @Param("member_id")Long member_id, 
						@Param("tm_grade")String tm_grade);

	// tm_grade 변경
	public int gradeModify(@Param("team_id")Long team_id, @Param("member_id")Long member_id, 
							@Param("tm_grade")String tm_grade);

	// team_member_id 검색
	public long myTeamMemberId(@Param("team_id")long team_id, @Param("member_id")long member_id);

}
