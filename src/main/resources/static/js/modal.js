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

const modalOutSideEvent = (event) => {
	const modalId = event.target;
	closeModal(modalId);
};
$("#signModal").click((event) => {
	modalOutSideEvent(event);
});
$("#authCodeModal").click((event) => {
	modalOutSideEvent(event);
});
$("#noticeModal").click((event) => {
	modalOutSideEvent(event);
});