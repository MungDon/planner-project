$(function() {
	$(".pwChangeBtn").click(() => {
		
		const member_id = $("#member_id").val();
		const newPassword = $("#newPassword").val();
		const newPassword2 = $("#newPassword2").val();
		const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{4,12}$/;
		if (isNull(member_id)) {
			const thenFn = () => {
				location.href = PAGE_LIST.MAIN_PAGE;
			};
			swalCall("경고", "권한이없습니다", "warning", thenFn);
		};
			if (newPassword !== newPassword2) {
				swalCall("경고", "비밀번호와 비밀번호 재확인의 입력 값이 일치하지않습니다.", "warning");
				return;
			}
			if (isNull(newPassword) || isNull(newPassword2)) {
				swalCall("경고", "비밀번호와 비밀번호 재확인의 입력 값은<br> 필수 입력입니다.", "warning");
				return;
			}
			if (!passwordRegex.test(newPassword)) {
				$("#failText").text('비밀번호는 최소 4자에서 12자까지, 영문자, 숫자 및 특수 문자를 포함해야합니다.');
				return;
			}

			const ajaxObj = {
				url: API_LIST.CHANGE_PASSWORD,
				method: "post",
				param: {
					member_id: member_id,
					newPassword: newPassword,
					newPassword2: newPassword2
				},
				successFn: (data) => {
					if (data === "ok") {
						const thenFn = () => {
							location.href = PAGE_LIST.LOGIN_PAGE;
						};
						swalCall("성공", "변경이 완료되었습니다.", "success",thenFn);
					}
				}
			};
			ajaxCall(ajaxObj);
	});
});