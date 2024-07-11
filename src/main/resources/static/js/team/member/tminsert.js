const tm_nickname = document.getElementById("tm_nickname");
const team_id = document.querySelector('meta[name="team_id"]').content;
const nick_check = document.getElementById("nick_check");
const submit_btn = document.getElementById("submit_btn");
const check_msg = document.getElementById("check_msg");

nick_check.addEventListener("click", function(){
	fetch("/team/member/nick-check?team_id="+team_id+"&tm_nickname="+tm_nickname.value)
		.then(response => response.text())
		.then(result => {
			if(result == 0){ // 중복 아님
				check_msg.style.color = 'blue';
				check_msg.innerText = '사용 가능한 별명입니다.';
				submit_btn.disabled = false;
			}else{ // 중복임
				check_msg.style.color = 'red';
				check_msg.innerText = '이미 사용중인 별명입니다.';
				submit_btn.disabled = true;
			}
		})
	;
});