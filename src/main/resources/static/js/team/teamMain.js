const links = document.querySelectorAll('span[data-link]');
// 링크 이동
links.forEach(link => {
	link.addEventListener("click", function(){
		location.href=this.dataset.link+"?team_id="+this.closest('div').dataset.team_id;
	})
});