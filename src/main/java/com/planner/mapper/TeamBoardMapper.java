package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.team.board.TeamBoardDTO;
import com.planner.dto.request.team.board.TeamBoardListDTO;
import com.planner.dto.request.team.board.TeamBoardUpdateDTO;

@Mapper
@Repository
public interface TeamBoardMapper {

	// 게시글 전체 개수
	public int teamBoardCountAll(Long team_id);

	// 검색된 게시글 개수 (전체)
	public int teamBoardCount(@Param("team_id")Long team_id, @Param("category")String category, 
							@Param("searchOption")String searchOption, @Param("search")String search);

	// 검색된 게시글 목록 (페이지)
	public List<TeamBoardListDTO> teamBoardList(@Param("team_id")Long team_id,@Param("category")String category, 
												@Param("searchOption")String searchOption, @Param("search")String search,
												@Param("start") int start,@Param("end") int end);

	// 게시글 등록
	public void teamBoardInsert(TeamBoardDTO dto);

	// 게시글 읽기
	public TeamBoardDTO teamBoardView(Long team_board_id);

	// 게시글 수정
	public void teamBoardUpdate(TeamBoardUpdateDTO dto);

	// 게시글 삭제
	public void teamBoardDelete(Long team_board_id);
}
