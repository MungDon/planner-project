$(function() {
	let prevImgNum = 0; // 이전 이미지 번호
	let prevScrollPos = 0;

	const updateImage = () => {
		let imgNum = Math.floor((Math.random() * 12)) + 1;
		if (imgNum === prevImgNum) {
			imgNum = prevImgNum+1;
			if(imgNum ==13){
				imgNum = 1;
			}
		}
		prevImgNum = imgNum;

		// 새 이미지 요소 생성
		let newImage = $('<img>', {
			'class': 'img',
			'src': '/images/' + imgNum + '.png'
		});
		// 현재 스크롤 위치 저장
		prevScrollPos = $(window).scrollTop();

		// 이미지 로드 완료 후 처리
		newImage.on('load', function() {
			// 기존 이미지 요소 제거
			$(".myeongUnBox").empty().append(newImage);

			// 스크롤 위치 조정
			$(window).scrollTop(prevScrollPos);
		});
	};

	// 페이지가 로드될 때 첫 이미지 설정
	updateImage();

	// 일정 시간마다 업데이트 (예: 15초마다)
	setInterval(updateImage, 15000); // 15000ms = 15초
});
const result = $("#result").val();

$(".addScheduleBtn").click(() => {
	location.href = PAGE_LIST.CALENDAR_PAGE;
});

$(".deleteBtn").click((event) => {
	const schedule_id = $(event.target).val();
	const thenFn = (result) => {
		if (result.isConfirmed) {
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
		}
	};
	swalCall("일정 삭제", "일정을 삭제하시겠습니까?", "question", thenFn, "예", true);
});

$(".noticeTitle").click((event) => {
	const notice_id = $(event.target).data("id");
	const ajaxObj = {
		url : API_LIST.NOTICE_DETAIL + notice_id,
		method : "get",
		successFn : (result) => {
			renderNoticeDetail(result);
			openModal("noticeModal");
		},
		errorFn : () =>{
			swalCall("경고","상세정보를 불러오지 못했습니다.","error");
		}
	};
	ajaxCall(ajaxObj);
});
const renderNoticeDetail = (details) => {
    const noticeDetailContainer = document.querySelector('.noticeDetail');
    noticeDetailContainer.innerHTML = ''; // 기존 내용을 지웁니다.

        noticeDetailContainer.innerHTML = `
        	    <div class='notice_title'>${details.notice_title}</div>
        `;
        let con = document.createElement("div");
        con.classList.add("notice_content")
        con.innerHTML = details.notice_content;
        noticeDetailContainer.append(con);
}
