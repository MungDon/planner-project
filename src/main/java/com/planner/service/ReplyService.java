package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.team.board.ReplyDTO;
import com.planner.dto.request.team.board.ReplyViewDTO;
import com.planner.mapper.ReplyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {
	
	private final ReplyMapper replyMapper;

	// 댓글 작성
	public int replyInsert(ReplyDTO dto){
		return replyMapper.replyInsert(dto);
	}

	// 댓글 목록 읽기
	public List<ReplyViewDTO> replyList(long team_board_id){
		return replyMapper.replyList(team_board_id);
	}

	// 댓글 삭제
	public void replyDelete(long reply_id) {
		replyMapper.replyDelete(reply_id);
	}

	// 댓글 수정
	public void replyUpdate(String reply_content, long reply_id) {
		replyMapper.replyUpdate(reply_content, reply_id);
	}
}
