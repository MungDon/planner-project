package com.planner.dto.request.schedule;

import java.time.LocalDateTime;

import lombok.Data;

/*
 CREATE TABLE schedule (
	schedule_id	number	primary key,
    member_id number NULL,
    team_id number null,
	schedule_title	varchar2(50)	NOT NULL,
	schedule_content	varchar2(500)	NULL,
	schedule_start	TIMESTAMP	NOT NULL,
	schedule_end	TIMESTAMP	NULL,
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (team_id) REFERENCES team(team_id) 
);
Create SEQUENCE schedule_seq;
 */

@Data
public class ScheduleDTO {
	private Long schedule_id;
	private String schedule_title;
	private String schedule_content;
	private LocalDateTime schedule_start;
	private LocalDateTime schedule_end;
	private String place;
	private String address;
	
	
}
