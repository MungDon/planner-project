/* 링크 클릭 시 직접 서밋 */
const infoA = document.querySelectorAll('.infoA');
infoA.forEach(button => {
	button.addEventListener('click', function () {
		const infoId = button.getAttribute('data-info');
		const infoForm = document.getElementById(infoId);
		infoForm.submit();
	});
});