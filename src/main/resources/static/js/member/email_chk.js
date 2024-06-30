$(function() {
	let csrfToken = $("meta[name='_csrf']").attr("content");
	let csrfHeader = $("meta[name='_csrf_header']").attr("content");
	$("#emailChkBox").css("display","none");
	
	// 이메일 인증 버튼 클릭시
	$(".emailChk").click(function() {
		const toEmail = $("#email").val();
		const inputCodeBox = $("#inputCodeBox");
		if (isNull(toEmail)) {
			alert("이메일을 입력해주세요");
			return;
		}
		ajaxCall("/member/anon/email/chk", "post", { toEmail },csrfHeader,csrfToken,
			function() {
				alert("인증 코드가 해당 이메일로 전송되었습니다.");
				inputCodeBox.css("display", "block");
			},
			function() {
				alert("이미 가입된 회원입니다");
				inputCodeBox.css("display", "none");
			});
	});

	// 인증 완료 버튼 눌렀을시
	$(".codeChk").click(function() {
		const toEmail = $("#email").val();
		const authCode = $("#inputCode").val();

		if(isNull(authCode)) {
			alert("인증코드를 입력해주세요");
			return;
		}
		ajaxCall("/member/anon/code/chk", "post", { toEmail, authCode },csrfHeader,csrfToken,
			function(data) {
				if(data=="ok"){
					alert("인증되었습니다.");
					$("#emailChkBox").prop("checked",true);
					$("#emailChkBox").css("display","block");
				}
			},
			function() {
				alert("인증실패");
			});
	});
});