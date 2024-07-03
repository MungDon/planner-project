$(function() {
	$(document).on("click", ".pwChkBtn", () => {
		const url = $(".pwChkBtn").val();
		const currentPw = $("#pw").val();
		// 회원탈퇴일시에 confirm으로 물어보기
		if (url === 'delete') {
			if (!confirm('정말 탈퇴하시겠습니까?')) {
				return false;
			}
		}
		console.log(url);
		const ajaxObj = {
			url: API_LIST.PASSWORD_CHK,
			method: "post",
			param: {
				currentPw: currentPw
			},
			successFn: (data) => {
				// 비밀번호 일치
				if (data === '성공') {
					// 들어온 url이 update냐 delete냐 나뉨
					// update 일때
					if (url === 'update') {
						location.href = PAGE_LIST.MEMBER_UPDATE_FORM;
					} else {
						const success_ajaxObj = {
							url: API_LIST.DELETE_MEMBER,
							method: "delete",
							successFn: () => {
								alert("탈퇴되었습니다.");
								location.href = PAGE_LIST.MAIN_PAGE;
							},
							errorFn: () => {
								alert("탈퇴실패.");
								location.href = PAGE_LIST.MAIN_PAGE;
							}
						};
						ajaxCall(success_ajaxObj);
					}
				}
			},
			errorFn: () => {
				alert('현재 비밀번호가 틀렸습니다.');
			}
		};
		ajaxCall(ajaxObj);
	});
});