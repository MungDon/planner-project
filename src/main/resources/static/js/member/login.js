$(function() {
	const result = $('#result').val();
	console.log(result);
	if (result == 1) {
		swalCall("성공", "가입되었습니다.", "success");
	}
	if (!isNull(result) && result != 1) {
		swalCall("에러", "가입 실패!", "error");
	}
});