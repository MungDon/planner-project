function listDelete() {
	var checkboxes = document.querySelectorAll("input[name='mapLikeListcheckbox']:checked");
	if (checkboxes.length > 0) {
		document.forms["deleteForm"].submit();
		return true;
	} else {
		alert('체크된 항목이 없습니다');
		return false;
	}
}

document.addEventListener("DOMContentLoaded", function() {
	var flashMessage = document.getElementById("flashMessage");
	if (flashMessage && flashMessage.textContent.trim() !== '') {
		alert(flashMessage.textContent);
	}
});

function listAdd(btn) {
	const td = btn.closest('td');
	const tr = td.parentNode;
	const place = tr.querySelector("#mapLikePlace").textContent.trim();
	const address = tr.querySelector("#mapLikeAddress").textContent.trim();
	if (id == 0) {
		console.log('id null');
		var mapPlace = document.getElementById('mapPlace');
		var mapAddress = document.getElementById('mapAddress');
		mapPlace.value = place;
		mapAddress.value = address;
		closeMapLikeModal();
		return true;
	} else if (id != null) {
		console.log('id 있음');
		var form2 = document.getElementById(id);
		form2.querySelector('.mapPlace').value = place;
		form2.querySelector('.mapAddress').value = address;
		closeMapLikeModal();
		return true;
	}
}


