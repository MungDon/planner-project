$(function() {
	$(document).ready(() => {
		const status = $("#status").val();
		if (status === null || status != 'B') {
			alert('권한이없습니다');
			window.location.href = PAGE_LIST.MAIN_PAGE;
		}
	});
});