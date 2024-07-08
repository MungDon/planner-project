$(function() {


	$(".pwChangeBtn").click(() => {
		const member_id = $("#member_id").val();
		const newPassword = $("#newPassword").val();
		const newPassword2 = $("#newPassword2").val();
		const passwordRegex = /^(?=(.*[a-z]){5,})(?=.*[!@])(?=.*\d).{7,}$/;
		if (isNull(member_id)) {
			Swal.fire({
				title: "경고",
				text: "권한이없습니다",
				icon: "warning",
				confirmButtonText: "확인"
			}).then(() => {
				location.href = PAGE_LIST.MAIN_PAGE;
			});
		}

		if (newPassword !== newPassword2) {
			Swal.fire({
				title: "경고",
				text: "비밀번호와 비밀번호 재확인의 입력 값이 일치하지않습니다.",
				icon: "warning",
				confirmButtonText: "확인"
			}).then(() => {
				return;
			});
		}
		if (isNull(newPassword) || isNull(newPassword2)) {
			Swal.fire({
				title: "경고",
				text: "비밀번호와 비밀번호 재확인의 입력 값은 필수 입력입니다.",
				icon: "warning",
				confirmButtonText: "확인"
			}).then(() => {
				return;
			});
		}
		if (!passwordRegex.test(newPassword)) {
			$("#failText").text('비밀번호는 최소 5개의 영어 소문자와 하나 이상의 특수기호(!, @), 숫자를 포함해야하고 7글자 이상이어야합니다.');
			return false;
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
					Swal.fire({
						title: "성공",
						text: "변경이 완료되었습니다.",
						icon: "success",
						confirmButtonText: "확인"
					}).then(() => {
						location.href = PAGE_LIST.LOGIN_PAGE;
					});
				}
			}
		};
		ajaxCall(ajaxObj);
	});
});