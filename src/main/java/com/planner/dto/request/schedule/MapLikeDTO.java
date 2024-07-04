package com.planner.dto.request.schedule;

import lombok.Data;

@Data
public class MapLikeDTO {
	private Long map_number;
	private Long member_id;
	private String map_title;
	private String map_address;
}
