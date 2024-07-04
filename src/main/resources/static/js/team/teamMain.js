const links = document.querySelectorAll('span[data-link]');

links.forEach(link => {
	link.addEventListener("click", function(){
		// 그룹 일정 페이지 만들고 나면 location.href = ~~ 이거만 남기면됨.
		if(this.dataset.link == 'calendar'){
			alert('페이지 미구현');
		}else{
			location.href=this.dataset.link+"?team_id="+this.closest('div').dataset.team_id;
		}
	})
});