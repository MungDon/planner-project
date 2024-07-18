const team_form = document.getElementById("team_form");
const team_name = document.getElementById("team_name");
const overlap_btn = document.getElementById("overlap_btn");
const overlap = document.getElementById("overlap");
const submit_btn = document.getElementById("submit_btn");
const img = document.getElementById('team_image');
const error_msg = document.getElementById("msg");
const fileFormat = /(jpg|jpeg|png|bmp|wbmp|gif|tif|tiff)/; // 허용할 이미지 확장자
const maxSize = 1*1024*1024;	// 최대 크기 2MB
const pass_msg = "사용 가능한 그룹 이름입니다.";
const overlap_msg = "이미 사용중인 그룹 이름입니다.";

// submit 했는데 오류생겨서 돌아온 경우
if(error_msg !== null){
	Swal.fire({
		title: "생성 실패",
		text: error_msg.value,
		icon: "error",
		confirmButtonText: "닫기"
	});
}

// 그룹 이름 중복 검사
overlap_btn.addEventListener("click", function(){
	team_name.value = team_name.value.trim();
	if(team_name.value.length < 2){
		Swal.fire({
			title: "그룹 이름 길이 제한",
			text: "2글자 이상 입력해주세요.",
			icon: "warning",
			confirmButtonText: "닫기"
		});
		return;
	}
	fetch("/team/overlap?team_name="+team_name.value)
		.then(response => response.text()) // response 객체의 값을 꺼내고
		.then(result => {				// 다음 then에서 사용. K:V면 text()말고 json() 사용
	 		overlap.style.display = 'block'
			if(result == 1){ // 중복 아님
		 		overlap.style.color = 'blue'
		 		overlap.innerText = pass_msg;
			}else{ // 중복임
		 		overlap.style.color = 'red'
		 		overlap.innerText = overlap_msg;
			}
		});
});

// 그룹 이름 바뀌면 중복검사 초기화
team_name.addEventListener("keydown", function(){
	overlap.style.display = 'none';
	overlap.innerText = '';
});

// submit 버튼
submit_btn.addEventListener("click", function(){
	if(overlap.innerText == pass_msg){ // 중복 검사 통과된 상태면
 		if(img.files[0] != null){ // 이미지 파일 있으면 검사
 			let filename = img.value.split('.');
 			if(filename[filename.length-1].toLowerCase().match(fileFormat) == null){
				Swal.fire({
					title: "미지원 포맷",
					text: "지원하지 않는 형식의 이미지 파일입니다.",
					icon: "warning",
					confirmButtonText: "닫기"
				});
 				return;
 			}
 			if(img.files[0].size > maxSize){
				Swal.fire({
					title: "용량 초과",
					text: "1MB 이하의 이미지를 사용해 주세요.",
					icon: "warning",
					confirmButtonText: "닫기"
				});
 				return;
 			}
 		}
 		team_form.submit();
	}else{
		Swal.fire({
			title: "중복 검사 필요",
			text: "그룹 이름 중복 검사가 필요합니다.",
			icon: "warning",
			confirmButtonText: "닫기"
		});
	}
});
