const team_id = document.getElementById("team_id").value;
const member_id = document.getElementById("member_id").value;
const update_btn = document.getElementById("update");
const delete_btn = document.getElementById("delete");
const header = document.querySelector('meta[name="_csrf_header"]').content;
const token = document.querySelector('meta[name="_csrf_token"]').content;
const headers = new Headers();
headers.append('header',header);
headers.append('X-CSRF-Token',token);

update_btn.addEventListener("click", function(){
	window.location='/team/member/update?team_id='+team_id+'&member_id='+member_id;
});

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