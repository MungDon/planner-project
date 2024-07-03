$(function() {
	
	
	$(".pwChangeBtn").click(() => {
		const member_id = $("#member_id").val();
		const newPassword = $("#newPassword").val();
		const newPassword2 = $("#newPassword2").val();
		console.log( $("#member_id").val());
	console.log( $("#newPassword").val());
	console.log($("#newPassword2").val());
		if(isNull(member_id)){
			alert("권한이없습니다");
			location.href = PAGE_LIST.MAIN_PAGE;
		}
		
		if(newPassword !== newPassword2){
			alert("비밀번호와 비밀번호 재확인의 입력 값이 일치하지않습니다.");
			return;
		}
		if(isNull(newPassword)||isNull(newPassword2)){
			alert("비밀번호와 비밀번호 재확인의 입력 값은 필수 입력입니다.");
			return;
		}
		
		const ajaxObj = {
			url : API_LIST.CHANGE_PASSWORD,
			method : "post",
			param : {
				member_id : member_id,
				newPassword : newPassword,
				newPassword2 : newPassword2
			},
			successFn: (data) => {
				if (data === "ok") {
					alert("변경이 완료되었습니다.");
					location.href = PAGE_LIST.LOGIN_PAGE;
				}
			}
		};
		ajaxCall(ajaxObj);
	});
});