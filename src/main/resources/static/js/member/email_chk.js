$(function() {
	let csrfToken = $("meta[name='_csrf']").attr("content");
	let csrfHeader = $("meta[name='_csrf_header']").attr("content");
	$("#emailChkBox").addClass("disabled-uncheck    m,.  ");

	// 새로고침 시 물어보기 ==== 크롬에서는 보안상 물어보는 메세지를 바꿀수없음
	$(window).on('beforeunload', function() {
		return null;
	});

	// 이메일 전송요청
	function sendEmail(toEmail,modalId) {
		if (isNull(toEmail)) {
			alert("이메일을 입력해주세요");
			return;
		}
		ajaxCall("/member/anon/email/chk", "post", { toEmail }, csrfHeader, csrfToken,
			function() {
				alert("인증 코드가 해당 이메일로 전송되었습니다.");
				if(!isNull(modalId)){
					openModal(modalId);
				}
			},
			function() {
				alert("이미 가입된 회원입니다");
			}
		);
	}
	// 이메일 인증 버튼 클릭시
	$(".emailChkBtn").click(function() {
		const toEmail = $("#email").val();
		$("#email").attr("readonly", true);
		sendEmail(toEmail,"authCodeModal");
	});
	// 인증번호 재선송 버튼 클릭 시
	$(".resendBtn").click(function() {
		const toEmail = $("#email").val();
		sendEmail(toEmail,null);
	});
	// 인증 완료 버튼 눌렀을시
	$(".codeChkBtn").click(function() {
		const toEmail = $("#email").val();
		const authCode = $("#inputCode").val();

		if (isNull(authCode)) {
			alert("인증코드를 입력해주세요");
			return;
		}
		ajaxCall("/member/anon/code/chk", "post", { toEmail, authCode }, csrfHeader, csrfToken,
			function(data) {
				if (data == "ok") {
					alert("인증되었습니다.");
					closeModal("authCodeModal");
					$("#emailChkBox").prop("checked", true);
					$("#emailChkBox").addClass("disabled-checkbox");
					$("#emailChkBox").css("display", "block");
					$("#emailChkBtn").css("display", "none");
				}
			},
			function() {
				alert("인증실패");
			});
	});
});