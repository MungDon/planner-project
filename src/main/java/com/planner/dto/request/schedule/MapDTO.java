package com.planner.dto.request.schedule;

import lombok.Data;

/*
Create table map(	
map_id number primary key,
schedule_id	number NOT NULL,
place varchar2(50),	
address varchar2(70),
FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id)
);
create SEQUENCE map_seq; 
*/

@Data
public class MapDTO {
	private Long  map_id;
	private Long schedule_id;
	private String place;
	private String address; 
}
