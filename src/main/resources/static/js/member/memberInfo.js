$(function() {
	$(document).ready( () => {
		const status = $("#status").val();
		if (status === null || status != 'B') {
			alert('권한이없습니다');
			window.location.href = '/planner/main';
		}
	});
	$(document).on("click", ".userDeleteBtn", () => {
		if (!confirm('정말 회원탈퇴를 하시겠습니까?')) {
			return false;
		}
		ajaxCall("/member/auth/delete", "delete", null,
			function() {
				location.href = "/planner/main";
			}, function() {
				alert("탈퇴실패");
			}
		);
	});
});

/* 친구 요청 거절버튼 클릭 시 value를 hidden으로 보냄 */
const sendDeleteBtn = document.getElementById('sendDeleteBtn');
const sendDelete = document.getElementById('sendDelete');
const sendDeleteH = document.getElementById('sendDeleteH');
const requestForm = document.getElementById('requestForm');
sendDeleteBtn.addEventListener('click', () => {
	sendDelete.value = sendDeleteH.value;
	requestForm.submit();
});