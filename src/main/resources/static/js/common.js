// null 체크공동 메서드
function isNull(chkData) {
	if (chkData == null || chkData == "") {
		return true;
	}
	return false;
}
// AJAX 공통 
function ajaxCall(url, method, param,csrfHeader, csrfToken, successFn, errorFn) {
	console.log(method);
	console.log(csrfHeader);
	console.log(csrfToken);
	$.ajax({
		url: url,
		method: method,
		data: param,
		beforeSend: function(xhr) {
			// CSRF 토큰을 요청 헤더에 포함
			xhr.setRequestHeader(csrfHeader, csrfToken);
		},
		success: function(data) {
			if (typeof successFn == "function") {
				successFn(data);
			}
		},
		error: function() {
			if (typeof errorFn == "function") {
				errorFn();
			}
		}
	});
};