/*document.getElementById("openMapBtn").onclick = function(){
  				var left = (window.screen.width - 830) / 2;
  				var top = (window.screen.height - 830) / 2;
  				window.open('mapForm', '_blank', 'width=809,height=708,left='+ left + ',top=' + top);
  				document.getElementById("mapModel").style.display = "block";
  				var date = document.getElementById("date").value;
  				console.log(date);
  			}
  			*/
  			
function openModal(btn) {
	const form = btn.closest('form');
    const signModal = document.getElementById("signModal");
    signModal.style.display = "block";
    relayout();
    var place = form.querySelector('.mapAddress');

    if(form){
		if(place == null){
			document.getElementById('insert_place').style.display = 'inline-block';
			document.getElementById('update_place').style.display = 'none';
		}else if (place.value.trim() != null){
			document.getElementById('insert_place').style.display = 'none';
			document.getElementById('update_place').style.display = 'inline-block';
		}else{
			console.error('값이 뭔가 이상함')
		}
	}// if (form) 끝
}// 펑션 끝

function closeModal() {
	const signModal = document.getElementById("signModal");
	signModal.style.display = "none";
}