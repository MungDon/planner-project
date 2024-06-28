$(function(){
	$(document).ready(function(){
		const status = $("#status").val();
		if(status === null || status != 'B'){
			alert('권한이없습니다');
                    window.location.href = '/planner/main';
		}
	});
	$(document).on("click",".userDeleteBtn",() => {
        let csrfToken = $("meta[name='_csrf']").attr("content");
        let csrfHeader = $("meta[name='_csrf_header']").attr("content");
		if(!confirm('정말 회원탈퇴를 하시겠습니까?')){
			return false;
		}
		$.ajax({
			url : "/member/auth/delete",
			type : "delete",
			beforeSend: function(xhr) {
                // CSRF 토큰을 요청 헤더에 포함
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
			success : function(){
				location.href="/planner/main";
			},
			error : function(){
				alert("탈퇴실패");
			}
		});
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