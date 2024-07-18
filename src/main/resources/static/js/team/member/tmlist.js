const member_count = document.getElementById("member_count");
const wait_count = document.getElementById("wait_count");
const team_id = document.querySelector('meta[name="team_id"]').content;
const tmlinks = document.querySelectorAll(".tmlink");
const modify = document.querySelectorAll(".modify");
const kick = document.querySelectorAll(".kick");
const accept = document.querySelectorAll(".accept");
const modify_grade = document.querySelectorAll(".modify_grade");
const modify_cancel = document.querySelectorAll(".modify_cancel");
const checkboxs = document.querySelectorAll('#grades input[type="checkbox"]');
const offset = 1000 * 60 * 60 * 9;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf_token"]').content;
const headers = new Headers();
headers.append('header',header);
headers.append('X-CSRF-Token',token);

// 그룹 인원 정보 이동
tmlinks.forEach(tmlink => {
	tmlink.addEventListener("click", function(){
		location.href = '/team/member/info?team_id='+team_id+'&member_id='+this.closest("tr").id;
	});
});

// grade 따라 표시 전환
checkboxs.forEach(e => {
	e.addEventListener("change", function(){
		let trs = document.querySelectorAll("."+e.id);
		if(e.checked == true){
			trs.forEach(tr => {
				tr.style.display = 'table-row';
			});
		}else{
			trs.forEach(tr => {
				tr.style.display = 'none';
			});
		}
	});
});

// 등급 수정 버튼
modify.forEach(e => {
	e.addEventListener("click", function(){
		let tr = e.closest("tr");
		tr.querySelector(".grade_select").style.display="block";
		tr.querySelector(".btn_grade").style.display="block";
		tr.querySelector(".grade_span").style.display="none";
		tr.querySelector(".btn_basic").style.display="none";
	});
});

// 등급 수정 완료 버튼
modify_grade.forEach(e => {
	e.addEventListener("click", function(){
		let tr = e.closest("tr");
		let tm_grade = tr.querySelector("select").selectedOptions[0].value;
		if(tm_grade == 'TEAM_MASTER'){ // 그룹장 양도인 경우 한번 더 확인
			let give = confirm("그룹장을 양도하시겠습니까?");
			if(!give){
				return;
			}
		}
		let data = new FormData();
		data.append('team_id', team_id);
		data.append('member_id', tr.id);
		data.append('tm_grade', tm_grade);
		let grade_request = new Request("/team/member/grade-modify", {
			method:'PATCH',
			headers:headers,
			body:data
		});
		fetch(grade_request)
		.then(response => {
			if(response.ok){
				tr.querySelector(".grade_select").style.display = "none";
				tr.querySelector(".btn_grade").style.display = "none";
				tr.querySelector(".grade_span").style.display = "inline";
				tr.querySelector(".btn_basic").style.display = "block";
				tr.querySelector(".grade_span").innerText = tm_grade;
				tr.className = tm_grade;
				if(tm_grade == 'TEAM_MASTER'){
					location.reload(true);
				}
			}else{
				Swal.fire({
					title: "요청이 실패했습니다.",
					icon: "error",
					confirmButtonText: "닫기"
				});
			}
		})
		;
	});
});

// 등급 수정 취소 버튼
modify_cancel.forEach(e => {
	e.addEventListener("click", function(){
		let tr = e.closest("tr");
		tr.querySelector(".grade_select").style.display="none";
		tr.querySelector(".btn_grade").style.display="none";
		tr.querySelector(".grade_span").style.display="inline";
		tr.querySelector(".btn_basic").style.display="block";
	});
});

// 추방
kick.forEach(e => {
	e.addEventListener("click", function(){
		let tr = e.closest("tr");
		let nick = tr.querySelectorAll("td")[1].querySelector("span").innerText;
		let alert_text;
		let confirm_text;
		if(e.classList.contains("wait")){
			alert_text = " 회원의 가입을 <br>거절하시겠습니까?";
			confirm_text = "가입을 거절했습니다.";
		}else{
			alert_text = " 회원을 그룹에서 <br>추방하시겠습니까?";
			confirm_text = "그룹에서 추방했습니다.";
		}
		Swal.fire({
			title: nick + alert_text,
			icon: "warning",
			showCancelButton: true,
			confirmButtonColor: "#d33",
			cancelButtonColor: "#3085d6",
			confirmButtonText: "예",
			cancelButtonText:"아니오"
		}).then((result) => {
			if (result.isConfirmed) {
				let member_id = tr.id;
				let data = new FormData();
				data.append('team_id', team_id);
				data.append('member_id', member_id);
				let del_request = new Request("/team/member/kick", {
					method:'DELETE',
					headers:headers,
					body:data
				});
				fetch(del_request)
				.then(response => {
					if(response.ok){
						if(this.className.includes("wait")){
							tr.remove();
							wait_count.innerText = wait_count.innerText - 1;
						}else{
							tr.remove();
							member_count.innerText = member_count.innerText - 1;
						}
						Swal.fire({
							title: confirm_text,
							icon: "success",
							confirmButtonText: "닫기"
						});
					}else{
						Swal.fire({
							title: "오류가 발생하였습니다.",
							icon: "error",
							confirmButtonText: "닫기"
						});
					}
				})
				.catch(() => {
					Swal.fire({
						title: "오류가 발생하였습니다.",
						icon: "error",
						confirmButtonText: "닫기"
					});
				})
				;
			}
		});
	});
});

// 가입승인
accept.forEach(e => {
	e.addEventListener("click", function(){
		let tr = e.closest("tr");
		let member_id = tr.id;
		let data = new FormData();
		data.append('team_id', team_id);
		data.append('member_id', member_id);
		let acc_request = new Request("/team/member/accept", {
			method:"PATCH",
			headers:headers,
			body:data
		});
		fetch(acc_request)
		.then(response => {
			if(response.ok){
				tr.querySelector(".grade_span").innerText='TEAM_USER';
				// offset으로 시간대 보정. (UTC+9)
				let today = new Date(Date.now() + offset).toISOString().substring(0, 10);
				tr.cells[3].innerText= today;
				tr.cells[4].innerText= '가입완료';
				wait_count.innerText = parseInt(wait_count.innerText) - 1;
				member_count.innerText = parseInt(member_count.innerText) + 1;
			}else{
				Swal.fire({
					title: "오류가 발생하였습니다.",
					icon: "error",
					confirmButtonText: "닫기"
				});
			}
		})
		.catch(() => {
			Swal.fire({
				title: "오류가 발생하였습니다.",
				icon: "error",
				confirmButtonText: "닫기"
			});
		})
		;
	});
});