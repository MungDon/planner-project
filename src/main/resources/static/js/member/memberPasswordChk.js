$(function() {
	$(document).on("click", ".pwChkBtn", function() {
		const url = $(this).val();
		const currentPw = $("#pw").val();
		// 회원탈퇴일시에 confirm으로 물어보기
		if (url === 'delete') {
			if (!confirm('정말 탈퇴하시겠습니까?')) {
				return false;
			}
		}
		ajaxCall("/member/auth/chk", "post", { currentPw },
			function(data) {
				// 비밀번호 일치
				if (data === '성공') {
					console.log(url);
					// 들어온 url이 update냐 delete냐 나뉨
					// update 일때
					if (url === 'update') {
						location.href = "/member/auth/" + url;
					} else {// delete 일때
						ajaxCall("/member/auth/delete", "delete", null,
							function() {
								alert("탈퇴되었습니다.");
								location.href = "/planner/main";
							},
							function() {
								alert("탈퇴실패.");
								location.href = "/planner/main";
							}
						);
					}
				}
			},
			function() {
				alert('현재 비밀번호가 틀렸습니다.');
			}
		);
	});
});