$(function() {
	// 소셜로그인 타입 박스 보여주기 안보여주기
	$(document).on("change", "#isOAuth", function() {

		if ($(this).is(":checked")) {// 체크 되었을 때
			$("#oauthTypeBox").css("display", "block");
			$("#currentPassword").css("display", "none");
		} else {					// 체크 해제 되었을 때
			$("#oauthTypeBox").css("display", "none");
			$("#currentPassword").css("display", "block");
		}
	});

	// 신청하기 버튼 클릭
	$(document).on("click", ".restoreBtn", function() {
		const currentEmail = $("#currentEmail").val();
		const currentPassword = $("#currentPassword").val();
		const oauth_type = $("#oauth_type").val();
		let csrfToken = $("meta[name='_csrf']").attr("content");
		let csrfHeader = $("meta[name='_csrf_header']").attr("content");

		if (currentEmail == null || currentEmail == "") {
			alert('이메일을 입력해주세요');
			return;
		}
		if ($("#isOAuth").is(":checked") && oauth_type == "") {
			alert('어떤 소셜로그인 종류인지 선택해주세요.');
			return;
		}
		if ((oauth_type == null || oauth_type == "") && (currentPassword == null || currentPassword == "")) {
			alert('일반로그인 이용 회원은 복구신청 시 비밀번호는 필수 입니다.');
			return;
		}

		$.ajax({
			url: "/member/anon/restore",
			type: "post",
			data: {
				currentEmail: currentEmail,
				currentPassword: currentPassword,
				oauth_type: oauth_type
			},
			beforeSend: function(xhr) {
				// CSRF 토큰을 요청 헤더에 포함 / 토큰 안넘기면 security에 걸려서 403에러뜸
				xhr.setRequestHeader(csrfHeader, csrfToken);
			},
			success: function(data) {
				switch (data) {
					case 0:
						alert("입력한 계정정보는 유효하지 않은정보입니다.");
						location.reload();
						break;
					case 1:
						alert("복구신청이 완료되었습니다. 복구는 영업일 수 2~3일 걸릴 수 도있습니다.");
						location.href = "/planner/main";
						break;
					case 2:
						alert("이미 신청 완료되었습니다. 자세한문의는 문의게시판에 작성해주세요");
						location.href = "/planner/main";
						break;
					case 3:
						alert("복구신청 대상이 아닙니다.");
						location.href = "/planner/main";
						break;
				}
			},
			error: function() {
				alert('복구신청이 실패하였습니다.');
				location.href = "/member/anon/login";
			}
		});

	});
});