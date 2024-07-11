package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.team.vote.VoteDTO;
import com.planner.dto.request.team.vote.VoteInfoDTO;

@Mapper
@Repository
public interface VoteMapper {

	// 투표 생성(투표 정보)
	public void voteInsert(VoteDTO dto);

	// 투표 생성(투표 항목)
	public void voteItemInsert(@Param("vote_id")Long vote_id, 
							@Param("vote_item_names")List<String> vote_item_names);

	// 투표 삭제
	public void voteDelete(Long vote_id);

	// 투표 정보 읽기
	public VoteInfoDTO voteInfo(Long vote_id);

	// 투표 유무 확인
	public int voteCheck(@Param("vote_id") Long vote_id,
						@Param("team_member_id")Long team_member_id);

	// 투표 인원 추가
	public int voteMemberInsert(@Param("vote_item_id") Long vote_item_id,
								@Param("team_member_id")Long team_member_id);
}
