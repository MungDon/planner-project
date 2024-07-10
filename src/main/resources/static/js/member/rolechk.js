$(function() {
	$(document).ready(() => {
		const status = $("#status").val();
		if (status === null || status != 'B') {
			const thenFn = () => {
				window.location.href = PAGE_LIST.MAIN_PAGE;
			};
			swalCall("경고", "권한이없습니다", "warning", thenFn);
   		}
	});
});