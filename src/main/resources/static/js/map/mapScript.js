function openModal(btn) {
	const form = btn.closest('form');
	const signModal = document.getElementById("signModal");
	const schedule_id = form.querySelector('.schedule_id');

	signModal.style.display = "block";
	relayout();

	if (schedule_id && schedule_id.length != 0) {
		id = schedule_id.value;
	} else {
		id = 0;
	}

	if (schedule_id != null) {
		document.getElementById('insert_place').style.display = 'none';
		document.getElementById('update_place').style.display = 'inline-block';
	} else if (schedule_id == null) {
		document.getElementById('insert_place').style.display = 'inline-block';
		document.getElementById('update_place').style.display = 'none';
	}

}

function closeModal() {
	const signModal = document.getElementById("signModal");
	signModal.style.display = "none";
}

function openMapLikeModal() {
   const a = document.getElementById("mapLikePlace");
   console.log(a);
   if(a == null  ){
      alert('즐겨찾기 목록이 없습니다!');
   }else{
   closeModal();
   const mapLikeModal = document.getElementById("MpaLikeModal");
   mapLikeModal.style.display = "block";
   }
}
function closeMapLikeModal() {
	const mapLikeModal = document.getElementById("MpaLikeModal");
	mapLikeModal.style.display = "none";
}

