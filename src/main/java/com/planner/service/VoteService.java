package com.planner.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.team.vote.VoteDTO;
import com.planner.dto.request.team.vote.VoteInfoDTO;
import com.planner.dto.request.team.vote.VoteMemberInsertDTO;
import com.planner.mapper.VoteMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {

	private final VoteMapper voteMapper;

	// 투표 생성(투표 정보)
	public void voteInsert(VoteDTO dto) {
		if (dto.getVote_title() != null && dto.getVote_end() != null) {
			voteMapper.voteInsert(dto);
		}
	}

	// 투표 생성(투표 항목)
	public void voteItemInsert(VoteDTO vdto, List<String> vote_item_names) {
		if (vdto.getVote_id() != null && vote_item_names.size() >= 2) {
			voteMapper.voteItemInsert(vdto.getVote_id(), vote_item_names);
		}
	}

	// 투표 삭제(글 삭제시)
	public void voteDelete(Long vote_id) {
		voteMapper.voteDelete(vote_id);
	}

	// 투표 정보 읽기
	public VoteInfoDTO voteInfo(Long vote_id) {
		return voteMapper.voteInfo(vote_id);
	}

	/**
	 * @return
	 * 1 : 요청 성공 <br>
	 * -1 : 요청 실패(투표 종료) <br>
	 * -2 : 요청 실패(중복 투표 시도) */
	// 투표한 인원 추가
	public int voteMemberInsert(VoteMemberInsertDTO dto) {
		// 현재시간과 투표 종료시간 비교
		if (LocalDateTime.now().isAfter(dto.getVote_end())) {
			return -1;
		}
		// 투표 이미 했는지 안했는지 비교
		int voted = voteMapper.voteCheck(dto.getVote_id(), dto.getTeam_member_id());
		if (voted == 1) {
			return -2;
		} else {
			// 투표한 인원에 추가
			return voteMapper.voteMemberInsert(dto.getVote_item_id(), dto.getTeam_member_id());
		}
	}
}
