const birthDate = document.getElementById("birth");
document.addEventListener('DOMContentLoaded', () => {
	birthDate.value = getTodayDate();
	birthDate.max = getTodayDate();
});
// 오늘날짜 가져오는 함수
const getTodayDate = () => {
	const today = new Date();
	const year = today.getFullYear() - 10;
	const month = String(today.getMonth() + 1).padStart(2, '0');
	const day = String(today.getDate()).padStart(2, "0");
	return year + "-" + month + "-" + day;
}

// 회원가입 폼 ONSUBMIT
const validateInsert = () => {
	const genderSelected = document.querySelector('input[name="member_gender"]:checked');
	const phone = document.getElementById("phone");
	const signChk = document.getElementById("signChk");
	const emailChkBox = document.getElementById("emailChkBox");
	const name = document.getElementById("name");
	const failText = document.getElementById("failText");
	const pw = document.getElementById('pw').value;
	const pw2 = document.getElementById('pw2').value
	const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{4,12}$/;
	if (!genderSelected) {
		// 라디오 버튼 중 하나도 선택되지 않은 경우 경고 메시지 표시 및 폼 제출 방지
		swalCall("경고", "성별을 선택해 주세요.", "warning");
		return false;
	}
	if (phone.value.length !== 11) {
		swalCall("경고", "전화번호가 알맞게 입력되었는지 확인해주세요.", "warning");
		return false;
	}
	if (name.value.trim().length === 1) {
		swalCall("경고", "이름은 두 글자 이상이어야합니다.", "warning");
		return false;
	}
	if (name.value.trim().length > 5) {
		swalCall("경고", "이름은 다섯 글자 이하이어야합니다.", "warning");
		return false;
	}
	if (pw !== pw2) {
		swalCall("경고", "비밀번호와 비밀번호 재확인의 입력 값이 다릅니다.", "warning");
		return false;
	}

	if (!signChk.checked) {
		swalCall("경고", "개인정보 수집에 동의해주세요.", "warning");
		return false;
	}
	if (!emailChkBox.checked) {
		swalCall("경고", "이메일 인증은 필수입니다.", "warning");
		return false;
	}
	if (birthDate.value > getTodayDate()) {
		swalCall("경고", "만 9세 이상만 가입 가능합니다.", "warning");
		return false;
	}
	if (birthDate.value < new Date('1900/01/01')) {
		swalCall("경고", "너 캡틴아메리카임?", "warning");
		return false;
	}
	if (!passwordRegex.test(pw)) {
		failText.innerHTML = '비밀번호는 최소 4자에서 12자까지, 영문자, 숫자 및 특수 문자를 포함해야합니다.';
		return false;
	}
	return true;
}