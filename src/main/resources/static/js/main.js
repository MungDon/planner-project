$(function() {
	let imgNum = Math.floor((Math.random() * 12)) + 1;
	const updateImage = () => {
		let imgNum2 = Math.floor((Math.random() * 12)) + 1;
		while (imgNum === imgNum2) {
			imgNum2 = Math.floor((Math.random() * 12)) + 1;
		}
		$(".myeongUnBox").html(
			'<img class="img" src="/images/' + imgNum2 + '.png">'
		);
		imgNum = imgNum2;
	}

	// 페이지가 로드될 때 첫 이미지 설정
	updateImage();

	// 일정 시간마다 업데이트 (예: 5초마다)
	setInterval(updateImage, 15000); // 5000ms = 5초
});
const result = $("#result").val();

$(".addScheduleBtn").click(() => {
	location.href = PAGE_LIST.CALENDAR_PAGE;
});

$(".deleteBtn").click((event) => {
	const schedule_id = $(event.target).val();
	const thenFn = (result) => {
		if (result.isDenied) {
			return;
		}
		const ajaxObj = {
			url: API_LIST.DELETE_SCHEDULE,
			method: "delete",
			param: {
				schedule_id: schedule_id
			},
			successFn: () => {
				const thenFn = () => {
					location.reload();
				};
				swalCall("성공", "삭제 성공!", "success", thenFn);
			},
			errorFn: () => {
				const thenFn = () => {
					location.href = PAGE_LIST.MAIN_PAGE;
				};
				swalCall("경고", "삭제 실패!", "error", thenFn);
			}
		};
		ajaxCall(ajaxObj);
	};
	swalCall("일정 삭제", "일정을 삭제하시겠습니까?", "question", thenFn, "예", true);
});
