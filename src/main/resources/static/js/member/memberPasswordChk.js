$(function() {
	$(document).on("click", ".pwChkBtn", () => {
		const url = $(".pwChkBtn").val();
		const currentPw = $("#pw").val();

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
						const thenFn = (result) => {
							if (result.isDenied) {
								return;
							}

							const success_ajaxObj = {
								url: API_LIST.DELETE_MEMBER,
								method: "delete",
								successFn: () => {
									const thenFn = () => {
										location.href = PAGE_LIST.MAIN_PAGE;
									};
									swalCall("성공", "탈퇴되었습니다.", "success", thenFn);
								},
								errorFn: () => {
									const thenFn = () => {
										location.href = PAGE_LIST.MAIN_PAGE;
									};
									swalCall("실패", "탈퇴실패.", "error", thenFn);
								}
							};

							ajaxCall(success_ajaxObj);
						};
						swalCall("회원탈퇴", "정말 회원탈퇴를 하시겠습니까?", "question", thenFn, "예", true);
					}
				}
			},
			errorFn: () => {
				swalCall("경고", "현재 비밀번호가 틀렸습니다.", "warning");
			}
		};
		ajaxCall(ajaxObj);
	});
});