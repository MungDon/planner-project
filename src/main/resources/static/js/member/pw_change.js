$(function(){
	$(".pwChangeBtn").click(() => {
		const member_id = $("#member_id").val();
		const newPassword = $("#newPassword").val();
		const newPassword2 = $("#newPassword").val();
		
		const successFn = (data) => {
			if(data ==="ok"){
				alert("변경이 완료되었습니다.");
				location.href="/member/anon/login";
			}
		};
		const errorFn = (errorResponse) => {
			const response = errorResponse.responsJSON;
			alert(response.message);
		};
		ajaxCall("/member/anon/pw/change","post",{member_id, newPassword, newPassword2},successFn,errorFn);
	});
});