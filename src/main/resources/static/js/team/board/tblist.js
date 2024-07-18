const listForm = document.getElementById("listForm")
const ps = document.getElementById("ps")
const ca = document.getElementById("ca")
const fold_notice = document.querySelectorAll(".fold");
const unfold = document.getElementById("unfold");
const fold = document.getElementById("fold");
// 공지사항이 4개 이상이라 fold하고 unfold가 있있으면
if(fold != null && unfold != null){
	// 공지사항 펼치기
	unfold.addEventListener("click", function(){
		unfold.style.display = 'none';
		fold_notice.forEach(element => {
			element.style.display = 'table-row';
		});
	});
	// 공지사항 접기
	fold.addEventListener("click", function(){
		unfold.style.display = 'table-row';
		fold_notice.forEach(element => {
			element.style.display = 'none';
		});
	});
}
// 페이지 사이즈 변경
ps.onchange = function(){
	let ops = listForm.ps.value;
	let nps = ps.options[ps.selectedIndex].value;
	let pn = listForm.pageNum.value;
	listForm.ps.value = nps;
	listForm.pageNum.value = Math.ceil((ops*(pn-1)+1)/nps); 
	listForm.submit();
}
// 카테고리 변경
ca.onchange = function(){
	listForm.ca.value = ca.options[ca.selectedIndex].value;
	listForm.pageNum.value = 1;
	listForm.submit();
}
// 검색어 검사
function searchCheck(){
	let search = listForm.search.value.trim();
	if(search.length != 1){
		listForm.pageNum.value = 1;
		return true;
	}else{
		Swal.fire({
			title: "검색어 길이 제한",
			text: "검색어는 2글자 이상 입력해주세요.",
			icon: "warning",
			confirmButtonText: "닫기"
		});
		return false;
	}
}