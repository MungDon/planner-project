$(function() {
	let imgNum = Math.floor((Math.random() * 12)) + 1;
    function updateImage() {
		let imgNum2= Math.floor((Math.random() * 12)) + 1;
		while(imgNum === imgNum2){
			imgNum2= Math.floor((Math.random() * 12)) + 1;
		}
        $(".myeongUnBox").html(
            '<img class="img" src="/images/' +imgNum2 + '.png">'
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
	Swal.fire({
		icon: "question",
		title: "일정을 삭제하시겠습니까?",
		showCancelButton: true,
		confirmButtonText: "예",
		cancelButtonText: "아니요"
	}).then((result) => {
		if (result.isConfirmed) {
			console.log("동작?");
			
			const ajaxObj = {
				url: API_LIST.DELETE_SCHEDULE,
				method: "delete",
				param: {
					schedule_id: schedule_id
				},
				successFn: () => {
					Swal.fire({
						title: "성공",
						text: "삭제 성공!",
						icon: "success",
						confirmButtonText: "확인"
					}).then(() => {
						location.reload();
					});
				},
				errorFn: () => {
					Swal.fire({
						title: "경고",
						text: "삭제 실패!",
						icon: "error",
						confirmButtonText: "확인"
					}).then(() => {
						location.href = PAGE_LIST.MAIN_PAGE;
					});
				}
			};
			ajaxCall(ajaxObj);
		}
	});
});
