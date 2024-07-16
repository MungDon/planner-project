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
	const modalId = event.target.id;
	closeModal(modalId);
};
$("#signModal").click((event) => {
	modalOutSideEvent(event);
});
$("#authCodeModal").click((event) => {
	$(".resendBtn").prop("disabled", false);
	$(".emailChkBtn").prop("disabled", false);
	$("#email").attr("readonly", false);
	modalOutSideEvent(event);
});
$("#noticeModal").click((event) => {
	modalOutSideEvent(event);
});

// 모달 내부 클릭 이벤트 방지
$('.modalBody').on('click', (event) => {
	event.stopPropagation();
});
// 모달 내부 클릭 이벤트 방지
$('.noticeModalBody').on('click', (event) => {
	event.stopPropagation();
});