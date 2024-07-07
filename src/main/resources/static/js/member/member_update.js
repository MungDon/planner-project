// 오늘날짜 가져오는 함수
const getTodayDate = () => {
	const today = new Date();
	const year = today.getFullYear() - 10;
	const month = String(today.getMonth() + 1).padStart(2, '0');
	const day = String(today.getDate()).padStart(2, "0");
	return year + "-" + month + "-" + day;
}

const validateUpdateForm = () =>{
	const phone = document.getElementById("phone");
	const name = document.getElementById("name");
	const birthDate = document.getElementById("birth");
	if (phone.value.length !== 11) {
		alert("전화번호가 알맞게 입력되었는지 확인해주세요.");
		return false;
	}
	if (name.value.trim().length === 1) {
		alert("이름은 두 글자 이상이어야합니다.");
		return false;
	}
	if (birthDate.value > getTodayDate()) {
		alert("만 9세 이상만 이용가능합니다.");
		return false;
	}
	return true;
}