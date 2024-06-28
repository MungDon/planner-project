$(function(){
	$(".emailChk").click(function(){
		const toEmail = $("#email").val();
		const inputCodeBox = $("#inputCodeBox");
		if(isNull(toEmail)){
			alert("이메일을 입력해주세요");
			return;
		}
		inputCodeBox.css("display","block");
		
		ajaxCall("/member/anon/email/chk","post",{toEmail},
		function(data){
			if(!isNull(data)){
				alert("인증 코드가 해당 이메일로 전송되었습니다.");
			}
		},
		function(){
			alert("인증코드 전송 실패!");			
		});
	});
	
});