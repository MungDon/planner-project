$(function() {
	$(document).on("click", ".pwChkBtn", function() {
		const url = $(this).val();
		const currentPw = $("#pw").val();
	    let csrfToken = $("meta[name='_csrf']").attr("content");
        let csrfHeader = $("meta[name='_csrf_header']").attr("content");
		// 회원탈퇴일시에 confirm으로 물어보기
		if (url === 'delete') {
			if (!confirm('정말 탈퇴하시겠습니까?')) {
				return false;
			}
		}
		// 입력한 비밀번호 체크
		$.ajax({
			url : "/member/auth/chk",
			type : "post",
			data : {
				currentPw : currentPw
			},
		     beforeSend: function(xhr) {
	                // CSRF 토큰을 요청 헤더에 포함
	                xhr.setRequestHeader(csrfHeader, csrfToken);
	            },
			success : function(data) {
				// 비밀번호 일치
				if (data === '성공') {
					console.log(url);
					// 들어온 url이 update냐 delete냐 나뉨
					// update 일때
					if (url === 'update') {
						location.href = "/member/auth/" + url;
					} else {// delete 일때
						$.ajax({
							url : "/member/auth/delete",
							type : "delete",
						     beforeSend: function(xhr) {
					                // CSRF 토큰을 요청 헤더에 포함
					                xhr.setRequestHeader(csrfHeader, csrfToken);
					            },
							success : function() {
								location.href = "/planner/main";
							}
						});
					}
				}
			},
			error : function() {
				alert('현재 비밀번호가 틀렸습니다.');
			}
		});
	});
});