$(function() {
	$(document).ready(() => {
		const status = $("#status").val();
		if (status === null || status != 'B') {
			const thenFn = () => {
				location.href = PAGE_LIST.MAIN_PAGE;
			};
			swalCall("경고", "권한이없습니다.", "warning", thenFn);
		}
	});

	$(document).on("click", ".userDeleteBtn", () => {
		const thenFn = () => {
			if (result.isConfirmed) {
				const ajaxObj = {
					url: API_LIST.DELETE_MEMBER,
					method: "delete",
					successFn: () => {
						const thenFn = () => {
							location.href = PAGE_LIST.MAIN_PAGE;
						};
						swalCall("성공", "탈퇴되었습니다", "success", thenFn);
					},
					errorFn: () => {
						const thenFn = () => {
							location.href = PAGE_LIST.MAIN_PAGE;
						};
						swalCall("에러", "탈퇴 못함 수구", "error", thenFn);
					}
				};
				ajaxCall(ajaxObj);
			} else{
				return;
			}
		};
		swalCall("회원탈퇴", "정말 회원탈퇴를 하시겠습니까?", "question", thenFn, "예", true);
	});
});

/* 친구 요청 거절버튼 클릭 시 value를 hidden으로 보냄 */
const sendDeleteBtn = document.getElementById('sendDeleteBtn');
const sendDelete = document.getElementById('sendDelete');
const sendDeleteH = document.getElementById('sendDeleteH');
const requestForm = document.getElementById('requestForm');
if(sendDeleteBtn !== null){
	sendDeleteBtn.addEventListener('click', () => {
		sendDelete.value = sendDeleteH.value;
		requestForm.submit();
	});
}