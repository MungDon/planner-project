$(function() {
	// 소셜로그인 타입 박스 보여주기 안보여주기
	$(document).on("change", "#isOAuth", function() {

		if ($(this).is(":checked")) {// 체크 되었을 때
			$("#oauthTypeBox").css("display", "block");
			$("#currentPassword").css("display", "none");
			$("#currentPassword").val("");

		} else {					// 체크 해제 되었을 때
			$("#oauthTypeBox").css("display", "none");
			$("#currentPassword").css("display", "block");
		}
	});

	// 신청하기 버튼 클릭
	$(document).on("click", ".restoreBtn", () => {
		const currentEmail = $("#currentEmail").val();
		const currentPassword = $("#currentPassword").val();
		const oauth_type = $("#oauth_type").val();

		if (currentEmail == null || currentEmail == "") {
			swalCall("경고", "이메일을 입력해주세요.", "warning");
			return;
		}
		if ($("#isOAuth").is(":checked") && oauth_type == "") {
			swalCall("경고", "어떤 소셜로그인 종류인지 선택해주세요.", "warning");
			return;
		}
		if ((oauth_type == null || oauth_type == "") && (currentPassword == null || currentPassword == "")) {
			swalCall("경고", "일반로그인 이용 회원은 복구신청 시 비밀번호는 필수 입니다.", "warning");
			return;
		}
		const ajaxObj = {
			url: API_LIST.MEMBER_RESTORE,
			method: "post",
			param: {
				currentEmail: currentEmail,
				currentPassword: currentPassword,
				oauth_type: oauth_type
			},
			successFn: (data) => {
				switch (data) {
					case 0:
						const restoreFail = () => {
							location.reload();
						};
						swalCall("경고", "입력한 계정정보는 유효하지 않은정보입니다.", "warning", restoreFail);
						break;
					case 1:
						const restoreSuccess = () => {
							location.href = PAGE_LIST.MAIN_PAGE;
						};
						swalCall("성공","복구신청이 완료되었습니다. 복구는 영업일 수 2~3일 걸릴 수 도있습니다.", "success", restoreSuccess);
						break;
				}
			},
		};
		ajaxCall(ajaxObj);
	});
});