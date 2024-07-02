// null 체크공동 메서드
const isNull = (chkData) => {
	if (chkData == null || chkData == "") {
		return true;
	}
	return false;
}

// CSRF 토큰
let csrfToken = $("meta[name='_csrf']").attr("content");
let csrfHeader = $("meta[name='_csrf_header']").attr("content");

// ajaxCall 호출시 errorFn을 안넘겨 줄때 디폴트로 넣어줄 유틸 함수
const defaultErrorFn = (errorResponse) => {
	const response = errorResponse.responseJSON;
	alert(response.message);
};
// AJAX 공통 
const ajaxCall = ({ url, method, successFn, param=null, errorFn = defaultErrorFn}) => {
	$.ajax({
		url: url,
		method: method,
		data: param,
		beforeSend: (xhr) => {
			// CSRF 토큰을 요청 헤더에 포함
			xhr.setRequestHeader(csrfHeader, csrfToken);
		},
		success: (data) => {
			if (typeof successFn == "function") {
				successFn(data);
			}
		},
		error: (xhr) => {
			if (typeof errorFn == "function") {
				errorFn(xhr);
			}
		}
	});
};