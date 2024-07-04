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

	public int teamBoardCountAll(Long team_id);
	
	public int teamBoardCount(@Param("team_id")Long team_id, @Param("category")String category, 
							@Param("searchOption")String searchOption, @Param("search")String search);

	public List<TeamBoardListDTO> teamBoardList(@Param("team_id")Long team_id,@Param("category")String category, 
												@Param("searchOption")String searchOption, @Param("search")String search,
												@Param("start") int start,@Param("end") int end);

	public void teamBoardInsert(TeamBoardDTO dto);

	public TeamBoardDTO teamBoardView(Long team_board_id);
	
	public void teamBoardUpdate(TeamBoardUpdateDTO dto);
	
	public void teamBoardDelete(Long team_board_id);
}
