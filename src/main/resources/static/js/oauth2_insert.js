  const birthDate = document.getElementById("birth");

document.addEventListener('DOMContentLoaded', () => {
	birthDate.value = getTodayDate();
	birthDate.max = getTodayDate();
});
// 오늘날짜 가져오는 함수
function getTodayDate() {
	const today = new Date();
	const year = today.getFullYear() - 10;
	const month = String(today.getMonth() + 1).padStart(2, '0');
	const day = String(today.getDate()).padStart(2, "0");
	return year + "-" + month + "-" + day;
}

// 회원가입 폼 ONSUBMIT
function validateOauth2Insert() {
	const genderSelected = document.querySelector('input[name="member_gender"]:checked');
	const phone = document.getElementById("phone");
	const signChk = document.getElementById("signChk");
	if (!genderSelected) {
		// 라디오 버튼 중 하나도 선택되지 않은 경우 경고 메시지 표시 및 폼 제출 방지
		alert('성별을 선택해 주세요.');
		return false;
	}
	if (phone.value.length !== 11) {
		alert("전화번호가 알맞게 입력되었는지 확인해주세요.");
		return false;
	}

	if (!signChk.checked) {
		alert("개인정보 수집에 동의해주세요.");
		return false;
	}
	if (birthDate.value > getTodayDate()) {
		alert("만 9세 이상만 가입 가능합니다.");
		return false;
	}
	return true;
}
function openModal(){
	 const signModal = document.getElementById("signModal");
	 signModal.style.display = "block";
}
function closeModal(){
	 const signModal = document.getElementById("signModal");
	 signModal.style.display = "none";
}