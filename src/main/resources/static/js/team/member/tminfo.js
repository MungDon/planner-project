const team_id = document.querySelector('meta[name="team_id"]').content;
const member_id = document.querySelector('meta[name="member_id"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf_token"]').content;
const form = document.getElementById("info_update");
const update_toggle = document.querySelectorAll(".update_toggle");
const update_btn = document.getElementById("update_btn");
const delete_btn = document.getElementById("delete");
const show = document.getElementById("show");
const hide = document.getElementById("hide");
const tm_nickname = document.getElementById("tm_nickname");
const nick_check = document.getElementById("nick_check");
const check_msg = document.getElementById("check_msg");
const headers = new Headers();
headers.append('header',header);
headers.append('X-CSRF-Token',token);

var org_nick = document.querySelector('meta[name="org_nick"]').content;

if(show != null){
	// form 수정 토글
	update_toggle.forEach(toggle => {
		toggle.addEventListener("click", function(){
			if(show.style.display == 'block'){ // 수정
				show.style.display = 'none';
				hide.style.display = 'block';
				form.tm_nickname.disabled = false;
				nick_check.style.display = 'block';
				check_msg.style.display = 'block';
				check_msg.closest("tr").style.display = 'table-row'
			}else{ // 수정 취소
				show.style.display = 'block';
				hide.style.display = 'none';
				form.tm_nickname.disabled = true;
				form.tm_nickname.value = org_nick;
				nick_check.style.display = 'none';
				check_msg.innerText = '중복검사가 필요합니다.';
				check_msg.style.color = 'red';
				check_msg.closest("tr").style.display = 'none'
				update_btn.disabled = true;
			}
		});
	})

	// form 수정 요청
	update_btn.addEventListener("click", function(){
		let update_request = new Request("/team/member/update", {
			method:'POST',
			body:new FormData(form)
		});
		fetch(update_request)
			.then(response => {
				if(response.status == 200){
					org_nick = form.tm_nickname.value;
					show.style.display = 'block';
					hide.style.display = 'none';
					form.tm_nickname.disabled = true;
					nick_check.style.display = 'none';
					check_msg.innerText = '중복검사가 필요합니다.';
					check_msg.style.color = 'red';
					check_msg.closest("tr").style.display = 'none'
					update_btn.disabled = true;
				}else{
					alert('중복된 별명입니다.');
				}
			})
			.catch(() => alert('서버 응답 실패'));
	});
	
	// 별명 중복 검사
	nick_check.addEventListener("click", function(){
	fetch("/team/member/nick-check?team_id="+team_id+"&tm_nickname="+tm_nickname.value)
		.then(response => response.text())
		.then(result => {
			if(result == 0){ // 중복 아님
				check_msg.style.color = 'blue';
				check_msg.innerText = '사용 가능한 별명입니다.';
				update_btn.disabled = false;
			}else{ // 중복임
				check_msg.style.color = 'red';
				check_msg.innerText = '이미 사용중인 별명입니다.';
				update_btn.disabled = true;
			}
		})
	;
});
}

if(delete_btn != null){
	// 그룹 탈퇴 버튼
	delete_btn.addEventListener("click", function(){
		let data = new FormData();
		data.append('team_id', team_id);
		data.append('member_id', member_id);
		let del_request = new Request("/team/member/delete", {
			method:'DELETE',
			headers:headers,
			body:data,
			redirect:"follow"
		});
		fetch(del_request)
			.then(response => {
				if(response.ok){
					// 그룹 탈퇴 요청 성공시 redirect
					window.location.href = response.url;
				}else{
					alert("탈퇴 요청 실패");
				}
			})
		;
	});
}