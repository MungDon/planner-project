// 오늘날짜 가져오는 함수
const getTodayDate = () => {
	const today = new Date();
	const year = today.getFullYear() - 10;
	const month = String(today.getMonth() + 1).padStart(2, '0');
	const day = String(today.getDate()).padStart(2, "0");
	return year + "-" + month + "-" + day;
}

const validateUpdateForm = () => {
	const phone = document.getElementById("phone");
	const name = document.getElementById("name");
	const birthDate = document.getElementById("birth");
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
	if(birthDate.value.split('-')[0] > new Date().getFullYear()){
		swalCall("경고", "타임머신 탔음?", "warning");
		return false;
	}
	if (birthDate.value > getTodayDate()) {
		swalCall("경고", "만 9세 이상만 이용가능합니다.", "warning");
		return false;
	}
	if (parseInt(birthDate.value.split('-')[0]) < 1900) {
		swalCall("경고", "너 캡틴아메리카임?", "warning");
		return false;
	}
	return true;
}