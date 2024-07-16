package com.planner.dto.request.team.vote;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {
	private Long vote_id;
	private Long team_board_id;
	private String vote_title;
	private String vote_content;
	private LocalDateTime vote_end;
}

/*
post 요청은 setter 없어도 값 바인딩됨.
get 요청은 setter 있어야 값 바인딩됨.
	-> setter 없이 하려면 아래같은 코드 추가되있어야함
			@ControllerAdvice
			public class WebControllerAdvice {
			
			    @InitBinder
			    public void initBinder(WebDataBinder binder) {
			        binder.initDirectFieldAccess();
			    }
			}

TeamBoardController 의 tbwrite 에서 받는데
여기선 Setter 없으면 값이 바인딩 안되고 다 null로 나옴.
파라미터를 dto 하나로 다 받는게 아니라 dto 2개와 @RequestParam 1개 해서
여러개로 쪼개 받아서 그런걸까 의심됨.

dto 하나에 다 담고, @RequestBody로 데이터를 받고, 스크립트로 json 형식으로 데이터를 넘겨봤는데
.... token `JsonToken.START_ARRAY` .... 하는 오류가 생김
jquery에서 form데이터를 serializeArray 후 json.stringfy 했는데
serializeObject를 사용하면 됬을지도??
*/