const orgContent = $("#orgContent").html();
const boardform = document.getElementById('boardform');

// 에디터 생성
$('#summernote').summernote({
	placeholder: '내용을 작성해 주세요.',
	tabDisable: true,
	height: 450,
	lang: 'ko-KR',
	disableDragAndDrop: true,
	toolbar: [
		['fontname', ['fontname']],
		['fontsize', ['fontsize']],
		['style', ['style','bold', 'italic', 'underline','strikethrough', 'clear']],
		['color', ['forecolor','color']],
		['table', ['table']],
		['para', ['ul', 'ol', 'paragraph']],
		['height', ['height']],
		['insert',['link']],
		['view', [ 'help']]
		],
	fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','sans-serif','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
	fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
});

// html태그, 공백 제거 (유효성 검사용)
function isEmpty(str_code){
  str_code = str_code.replace(/<\/?[^>]*>/gi,"")
  str_code = str_code.replace(/&nbsp;/gi, "");
  return str_code.trim();
}

// submit (유효성 검사 포함 (내용이 있는지 없는지))
document.getElementById('btn_save').onclick = function(){
	let title = document.getElementById('tb_title').value;
	let content = document.querySelector('.note-editable').innerHTML
	if(title.trim().length < 2){
		alert('2글자 이상 제목을 입력해 주세요');
	}else if(isEmpty(content).length == 0){
		alert('내용을 입력해 주세요');
	}else{
		boardform.tb_content.value = content;
		boardform.submit();
	}
}

// 리셋 버튼
document.querySelector('.note-editable').innerHTML = orgContent;
document.querySelector('.note-placeholder').style='display: none';
document.getElementById('btn_reset').addEventListener("click", function(){
	boardform.reset();
	document.querySelector('.note-editable').innerHTML = orgContent;
});