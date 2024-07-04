const form = document.getElementById("searchForm");
const pages = document.querySelectorAll(".page");
const teams = document.querySelectorAll(".team");
// 검색 글자수 2글자 이상
function submitCheck(){
	if(form.search.value.trim().length >= 2){
		return true;
	}else{
		alert("검색어는 2글자 이상 입력해주세요.");
		return false;
	}
}

// 페이지 번호 누르면 form에 page 값 변경하고 submit
pages.forEach(page => {
	page.addEventListener("click", function(){
		form.page.value = this.dataset.page;
		form.submit();
	});
});

// team 누르면 info 페이지로 이동
teams.forEach(team => {
	team.addEventListener("click", function(){
		let team_id = this.dataset.team_id;
		window.location = "/team/info?team_id="+team_id;
	});
});