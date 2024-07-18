package com.planner.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.schedule.MapLikeDTO;


	
@Mapper
@Repository
public interface MapLikeMapper {
	// 즐겨찾기 저장 
	public void MapLikeInsert(MapLikeDTO dto);
	
	// 즐겨찾기 목록
	public ArrayList<MapLikeDTO> MapLikeSelect(@Param(value = "member_id") Long member_id);
	
	// 선택된 즐겨찾기 목록
	public List<MapLikeDTO> MapLikeAllSelect(@Param("member_id")Long member_id,@Param("map_number")Long map_number);
	
	// 즐겨찾기 삭제
	public int  MapLikeDelete(@Param("member_id")Long member_id ,@Param("map_numbers")List<Integer> mapLikeListcheckbox);
	
	// 중복값 검사 
	public int mapLikeDuplicate(MapLikeDTO dto); 
}
	
