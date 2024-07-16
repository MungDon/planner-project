$(".saveBtn").click(() => {
	const noticeForm = $("#noticeForm");
	const editorContent = $(".note-editable").html();
	noticeForm.find('textarea[name="notice_content"]').val(editorContent);
	validateNoticeForm(noticeForm);
});

const validateNoticeForm = (noticeForm) => {
	const title = $('#notice_title').val();
	const content = $('textarea[name="notice_content"]').val();
	if (isNull(title)) {
		swalCall("경고", "공지사항의 제목을 입력해주세요.", "warning");
		return;
	}
	if (title.length > 50) {
		swalCall("경고", "공지사항의 제목을 50자 내로 입력해주세요.", "warning");
		return;
	}
	if (isNull(content)) {
		swalCall("경고", "공지사항의 내용을 입력해주세요.", "warning");
		return;
	}
	noticeForm.submit();
};