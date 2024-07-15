// 모달창 켜기
const openModal = (modalId) => {
	const modal = document.getElementById(modalId);
	modal.style.display = "block";
}

// 모달창 끄기
const closeModal = (modalId) => {
	const modal = document.getElementById(modalId);
	modal.style.display = "none";
}

/*const modalOutSideEvent = (modalId) => {
	closeModal(modalId);
};
$("#signModal").click(()=>{
	modalOutSideEvent($(this));
});*/