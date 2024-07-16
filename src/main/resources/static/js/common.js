// null 체크공동 메서드
const isNull = (chkData) => {
	if (chkData == null || chkData == "") {
		return true;
	}
	return false;
}

// ajaxCall에서 사용할 api 주소를 상수로 관리
const API_LIST = {
	EMAIL_SEND: "/member/anon/email/send",
	AUTH_CODE_CHK: "/member/anon/code/chk",
	DELETE_MEMBER: "/member/auth/delete",
	PASSWORD_CHK: "/member/auth/pw/chk",
	MEMBER_RESTORE: "/member/anon/restore",
	CHANGE_PASSWORD: "/member/anon/pw/change",
	DELETE_SCHEDULE: "/planner/schedule/del",
	NOTICE_DETAIL : "/notice/detail/",
	DELETE_NOTICE_IMG : "/admin/img/delete"
}

// 단순 페이지 이동 url 상수
const PAGE_LIST = {
	MAIN_PAGE: "/planner/main",
	CHANGE_PASSWORD_FORM: "/member/anon/pw/change/",
	MEMBER_UPDATE_FORM: "/member/auth/update",
	LOGIN_PAGE: "/member/anon/login",
	CALENDAR_PAGE: "/planner/calendar"
};
// CSRF 토큰
let csrfToken = $("meta[name='_csrf']").attr("content");
let csrfHeader = $("meta[name='_csrf_header']").attr("content");

// ajaxCall 호출시 errorFn을 안넘겨 줄때 디폴트로 넣어줄 유틸 함수
const defaultErrorFn = (errorResponse) => {
	const response = errorResponse.responseJSON;
	swalCall("경고",response.message,"error");
};
// AJAX 공통			// 구조 분해할당
const ajaxCall = ({url, method, successFn, param = null, errorFn = defaultErrorFn }) => {
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

/*스윗 알러트 공통*/
const swalCall = (title, text, icon, thenFn, confirmButtonText = "확인", showCancelButton = false, cancelButtonText = "아니요") => {
	Swal.fire({
		title: title,
		html: text,
		icon: icon,
		confirmButtonText: confirmButtonText,
		showCancelButton: showCancelButton,
		cancelButtonText: cancelButtonText
	}).then((result) => {
		if (typeof thenFn == "function") {
			thenFn(result);
		}
	});
};