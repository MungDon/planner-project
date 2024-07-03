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
	const passwordRegex = /^(?=(.*[a-z]){5,})(?=.*[!@])(?=.*\d).{7,}$/;
	if (!genderSelected) {
		// 라디오 버튼 중 하나도 선택되지 않은 경우 경고 메시지 표시 및 폼 제출 방지
		alert('성별을 선택해 주세요.');
		return false;
	}
	if (phone.value.length !== 11) {
		alert("전화번호가 알맞게 입력되었는지 확인해주세요.");
		return false;
	}
	if (name.value.trim().length === 1) {
		alert("이름은 두 글자 이상이어야합니다.");
		return false;
	}
	if (pw !== pw2) {
		alert("비밀번호와 비밀번호 재확인의 입력 값이 다릅니다.");
		return false;
	}

	if (!signChk.checked) {
		alert("개인정보 수집에 동의해주세요.");
		return false;
	}
	if (!emailChkBox.checked) {
		alert("이메일 인증은 필수입니다.");
		return false;
	}
	if (birthDate.value > getTodayDate()) {
		alert("만 9세 이상만 가입 가능합니다.");
		return false;
	}
	if (!passwordRegex.test(pw)) {
		failText.innerHTML = '비밀번호는 최소 5개의 영어 소문자와 하나 이상의 특수기호(!, @), 숫자를 포함해야하고 7글자 이상이어야합니다.';
		return false;
	}
	return true;
}