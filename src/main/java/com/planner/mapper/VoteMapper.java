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
	public void voteInsert(VoteDTO dto);
	public void voteItemInsert(@Param("vote_id")Long vote_id, 
							@Param("vote_item_names")List<String> vote_item_names);
	public void voteDelete(Long vote_id);
	public VoteInfoDTO voteInfo(Long vote_id);
	public int voteCheck(@Param("vote_id") Long vote_id,
						@Param("team_member_id")Long team_member_id);
	public int voteMemberInsert(@Param("vote_item_id") Long vote_item_id,
								@Param("team_member_id")Long team_member_id);
}
