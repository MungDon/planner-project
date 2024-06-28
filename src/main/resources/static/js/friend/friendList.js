/* 반복문으로 꺼낸 숨김버튼에 속성을 부여함 */
const hideBtns = document.querySelectorAll('.hideBtn');
hideBtns.forEach(button => {
	button.addEventListener('click', () => {
		const hideId = button.getAttribute('data-hide');
		const showId = button.getAttribute('data-show');
		const submitId = button.getAttribute('data-submit');
		const hiddenId = button.getAttribute('data-hidden');
		const nickId = button.getAttribute('data-nick');
		const nameT = document.getElementById(hideId);
		const nameI = document.getElementById(showId);
		const submitBtn = document.getElementById(submitId);
		const nameH = document.getElementById(hiddenId);
		const nickF = document.getElementById(nickId);button
		
		/* 클릭 시 다른 버튼 비활성화 */
		hideBtns.forEach(btn => {
			if (btn !== button) {
				btn.disabled = true;
			}
		});
		
		if (nameT) {
			nameT.style.display = 'none';
		}
		if (nameI && submitBtn) {
			submitBtn.style.display = 'inline-block';
			nameI.style.display = 'inline-block';
		}
		
		/* 버튼타입을 직접 서밋 */
		submitBtn.addEventListener('click', () => {
			nameH.value = nameI.value;
			nickF.submit();
		});
		button.style.display = 'none';
	});
});

/* 링크 클릭 시 직접 서밋 */
const infoA = document.querySelectorAll('.infoA');
infoA.forEach(button => {
	button.addEventListener('click', () => {
		const infoId = button.getAttribute('data-info');
		const infoForm = document.getElementById(infoId);
		infoForm.submit();
	});
});