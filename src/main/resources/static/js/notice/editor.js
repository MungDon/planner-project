$(function() {
	// CSRF 토큰
	let csrfToken = $("meta[name='_csrf']").attr("content");
	let csrfHeader = $("meta[name='_csrf_header']").attr("content");


	$('#summernote').summernote({
		// 에디터 크기 설정
		height: 800,
		// 에디터 한글 설정
		lang: 'ko-KR',
		// 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
		toolbar: [
			// 글자 크기 설정
			['fontsize', ['fontsize']],
			// 글자 [굵게, 기울임, 밑줄, 취소 선, 지우기]
			['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
			// 글자색 설정
			['color', ['color']],
			// 표 만들기
			['table', ['table']],
			// 서식 [글머리 기호, 번호매기기, 문단정렬]
			['para', ['ul', 'ol', 'paragraph']],
			// 줄간격 설정
			['height', ['height']],
			// 이미지 첨부
			['insert', ['picture']]
		],
		// 추가한 글꼴
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', '맑은 고딕', '궁서', '굴림체', '굴림', '돋음체', '바탕체'],
		// 추가한 폰트사이즈
		fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72', '96'],
		// focus는 작성 페이지 접속시 에디터에 커서를 위치하도록 하려면 설정해주세요.
		focus: true,
		callbacks: {
			onImageUpload: function(files) {
				// 다중 이미지 처리를 위해 for문을 사용했습니다.
				for (var i = 0; i < files.length; i++) {
					imageUploader(files[i], this);
				}
			},
			onMediaDelete: function($target) {
					let deletedImageUrl = $target
							.attr('src')
							.split('/')
							.pop()
				const thenFn = (result) => {
					if (result.isConfirmed) {
						// ajax 함수 호출
						deleteImageFile(deletedImageUrl)
						return;
					}else{
						$('#summernote').summernote('insertImage', "/noticeImg/" + deletedImageUrl);
						return;
					}
				};
				swalCall("이미지삭제","이미지를 삭제 하시겠습니까?","question",thenFn,"예",true);
			}
		}

	});
	const imageUploader = (file, el) => {
		let formData = new FormData();
		formData.append('file', file);

		$.ajax({
			data: formData,
			type: "POST",
			url: '/admin/upload/img',
			contentType: false,
			processData: false,
			enctype: 'multipart/form-data',
			beforeSend: (xhr) => {
				// CSRF 토큰을 요청 헤더에 포함
				xhr.setRequestHeader(csrfHeader, csrfToken);
			},
			success: function(data) {
				$(el).summernote('insertImage', "/noticeImg/" + data);
				console.log(data);
			}
		});
	};
	const deleteImageFile = (deletedImageUrl) =>{
		const ajaxObj = {
			url : API_LIST.DELETE_NOTICE_IMG,
			method : "delete",
			param : {
				imgName : deletedImageUrl
			},
			successFn : (result) =>{
				if(!isNull(result)){
					swalCall("성공","이미지를 삭제 하였습니다.","success");
				}	
			}
		};
		ajaxCall(ajaxObj);
	};
	
	$(".saveBtn").click(()=>{
		const noticeForm = $("#noticeForm");
		const editorContent= $(".note-editable").html();
		noticeForm.find('textarea[name="notice_content"]').val(editorContent);
		noticeForm.submit();
	});
});