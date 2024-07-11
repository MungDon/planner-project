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

	// 그룹 이름 중복 체크
	public int teamNameOverlap(String team_name);
	
	// 그룹 생성
	public void teamInsert(TeamDTO dto);
	
	// 그룹 정보 수정
	public void teamUpdate(TeamDTO dto);
	
	// 그룹 제거
	public int teamDelete(@Param("member_id") long member_id, @Param("team_id")long team_id);
	
	// 그룹 정보 읽기
	public TeamDTO teamInfo(long team_id);
	
	// 그룹 정보 + 그룹장 이메일
	public TeamInfoDTO teamAndMasterInfo(long team_id);
	
	// 내 그룹 목록
	public List<MyTeamListDTO> myTeamList(Long member_id);
	
	// 그룹 목록 검색(페이지처리 되어있음)
	public List<TeamInfoDTO> teamListSearch(@Param("start")long start, @Param("end")long end,
										@Param("searchOption")String searchOption,
										@Param("search")String search);
	
	// 검색된 그룹 목록 개수
	public long teamCount(@Param("searchOption")String searchOption, @Param("search")String search);

}
