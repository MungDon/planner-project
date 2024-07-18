function listDelete() {
	const checkboxes = document.querySelectorAll("input[name='mapLikeListcheckbox']:checked");
	const mapNumbers = Array.from(checkboxes).map(cb => cb.value);
	const ajaxObj = {
		url: "/mapLikeDelete",
		method: "post",
		param: {
			mapLikeListcheckbox: mapNumbers
		},
		successFn: (data) => {
			swalCall("성공", "삭제되었습니다!", "success")
			closeMapLikeModal();	
		}
	}
	ajaxCall(ajaxObj);
};



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
		var mapPlace = document.getElementById('mapPlace');
		var mapAddress = document.getElementById('mapAddress');
		mapPlace.value = place;
		mapAddress.value = address;
		closeMapLikeModal();
		return true;
	} else if (id != null) {
		var form2 = document.getElementById(id);
		form2.querySelector('.mapPlace').value = place;
		form2.querySelector('.mapAddress').value = address;
		closeMapLikeModal();
		return true;
	}
}


