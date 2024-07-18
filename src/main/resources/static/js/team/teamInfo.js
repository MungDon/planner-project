const check_delete = document.getElementById('check_delete');
const del_form = document.getElementById('del_form');

check_delete.addEventListener('click', function(){
	Swal.fire({
		title: "그룹을 삭제하시겠습니까?",
		text: "삭제 후 되돌릴 수 없습니다.",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#d33",
		cancelButtonColor: "#3085d6",
		confirmButtonText: "삭제",
		cancelButtonText: "취소"
	})
	.then((result) => {
		if (result.isConfirmed) {
		del_form.submit();
		}
	});
})