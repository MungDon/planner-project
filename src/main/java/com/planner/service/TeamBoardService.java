package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.team.board.TeamBoardDTO;
import com.planner.dto.request.team.board.TeamBoardListDTO;
import com.planner.dto.request.team.board.TeamBoardUpdateDTO;
import com.planner.mapper.TeamBoardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamBoardService {

	private final TeamBoardMapper teamBoardMapper;
	private final TeamMemberService tmService;

	// 전체 게시글 개수
	public int tbCountAll(Long team_id) {
		return teamBoardMapper.teamBoardCountAll(team_id);
	}

	// 검색된 게시글 개수
	public int tbCount(Long team_id, String category, String searchOption, String search) {
		return teamBoardMapper.teamBoardCount(team_id, category, searchOption, search);
	}

	// 검색된 게시글 목록
	public List<TeamBoardListDTO> tblist(Long team_id, String category, String searchOption, String search, int pageNum, int pageSize){
		int start = (pageNum-1)*pageSize + 1;
		int end = pageSize * pageNum;
		return teamBoardMapper.teamBoardList(team_id, category, searchOption, search, start, end);
	}

	// 게시글 등록
	public void tbInsert(TeamBoardDTO dto, long member_id) {
		long team_member_id = tmService.teamMemberId(dto.getTeam_id(), member_id);
		dto.setTeam_member_id(team_member_id);
		teamBoardMapper.teamBoardInsert(dto);
	}

	// 게시글 읽기
	public TeamBoardDTO teamBoardView(Long team_board_id) {
		return teamBoardMapper.teamBoardView(team_board_id);
	}

	// 게시글 수정
	public void teamBoardUpdate(TeamBoardUpdateDTO dto) {
		teamBoardMapper.teamBoardUpdate(dto);
	}

	// 게시글 삭제
	public void teamBoardDelete(Long team_board_id) {
		teamBoardMapper.teamBoardDelete(team_board_id);
	}
}
