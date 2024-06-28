// 	별명 수정
const hideBtn = document.getElementById('hideBtn');
const nameT = document.getElementById('nameT');
const nameI = document.getElementById('nameI');
const submitBtn = document.getElementById('submitBtn');
const nameH = document.getElementById('nameH');
const nickForm = document.getElementById('nickForm');

/* friendList 와 다르게 반복되는 버튼 아님 */
hideBtn.addEventListener('click', () => {
	if (nameT) {
		nameT.style.display = 'none';
	}
	if (nameI && submitBtn) {
		submitBtn.style.display = 'inline-block';
		nameI.style.display = 'inline-block';
	}
	submitBtn.addEventListener('click', () => {
		nameH.value = nameI.value;
		nickForm.submit();
	});
	hideBtn.style.display = 'none';
	hideMemoBtn.disabled = 'true';
});
		
// 	메모 수정
const hideMemoBtn = document.getElementById('hideMemoBtn');
const memoT = document.getElementById('memoT');
const memoI = document.getElementById('memoI');
const submitMemoBtn = document.getElementById('submitMemoBtn');
const memoH = document.getElementById('memoH');
const memoForm = document.getElementById('memoForm');

hideMemoBtn.addEventListener('click', () => {
	if (memoT) {
		memoT.style.display = 'none';
	}
	if (memoI && submitMemoBtn) {
		submitMemoBtn.style.display = 'inline-block';
		memoI.style.display = 'inline-block';
	}
	submitMemoBtn.addEventListener('click', () => {
		memoH.value = memoI.value;
		memoForm.submit();
	});
	hideMemoBtn.style.display = 'none';
	hideBtn.disabled = 'true';
});
		
/* 친구삭제 버튼 클릭 시 직접 서밋 */
const delBtn = document.getElementById('deleteBtn');
const delForm = document.getElementById('deleteForm');
delBtn.addEventListener('click', () => {
	if (confirm('삭제 하시겠습니까?')){
		delForm.submit();
	}
});