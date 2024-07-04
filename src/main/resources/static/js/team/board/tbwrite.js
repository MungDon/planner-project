const boardform = document.getElementById('boardform');

// 투표 활성화, 비활성화
function vote_toggle(){
	$("#vote").toggle();
	if($("#vote").css("display") == "none"){
		$("#vote_content").attr("disabled", true); 
		$("#vote input").each(function(){
			$(this).attr("disabled", true); 
		});
	}else{
		$("#vote_content").attr("disabled", false); 
		$("#vote input").each(function(){
			$(this).attr("disabled", false); 
		});
		let min = new Date(Date.now() + (60000*60*10)); // 한국 기준 현재 시간 + 1시간
		$("#vote_end").val(min.toISOString().substring(0, 17)+'00'); // 초단위 제거
	}
}
// 투표 항목 추가
$("#new_item").on("click", function(){
	if($(".vote_item").length < 10){ // 항목 개수 제한
		$("#item").append('<input type="text" name="vote_item_name" class="vote_text vote_item new_item" placeholder="항목을 입력해주세요"><button type="button" class="btn del_item">제거</button>');
	}else{
		alert("투표 항목은 최대 10개 입니다.");
	}
});
/* 
	index로 접근했을 때
	$('선택자').eq(2); //jQuery객체 반환
	$('선택자').get(2); //DOM개체 반환
	$('선택자')[2]; //DOM개체 반환
*/
// 투표 추가 항목 제거
$(document).on("click", ".del_item", function(){
	$(".new_item").get( $(".del_item").index(this) ).remove();
	this.remove();
});
// 투표 버튼
var vote_btn = function (context) {
	var ui = $.summernote.ui;

	// create button
	var button = ui.button({
		contents: '<i/>투표',
		click: function () {
			context.invoke(vote_toggle());
		}
	});
	return button.render();
}

// 일정 버튼
var schedule_btn = function (context) {
	var ui = $.summernote.ui;

	// create button
	var button = ui.button({
		contents: '<i class="fa fa-child"/> 일정',
		click: function () {
			context.invoke(alert('미구현'));
		}
	});
	return button.render();
}

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
		['view', [ 'help']],
		['mybutton', ['vote', 'schedule']]
		],
	buttons: {
		vote: vote_btn,
		schedule: schedule_btn
	},
	fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','sans-serif','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
	fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
});

// html태그, 공백 제거 (유효성 검사용)
function isEmpty(str_code){
  //var ptn = new RegExp("<\/?[^>]*>","gi");
  //str_code = str_code.replace(ptn,"")
  str_code = str_code.replace(/<\/?[^>]*>/gi,"")
  str_code = str_code.replace(/&nbsp;/gi, "");
  return str_code.trim();
}
// 투표 종료 시간 검사
function timecheck(){
	let vote_end = $("#vote_end");
	let min = new Date(Date.now() + 39300000); // 현재시간 + 9시간(UTC+9) + 55분
	let max = new Date(Date.now() + 637500000); // 현재시간 + 9시간(UTC+9) + 7일 5분 
	if(vote_end.val() < min.toISOString()){
		alert("투표 종료 시간은 최소 1시간 이후입니다.");
		vote_end.val(min.toISOString().substring(0, 17)+'00');
		return false;
	}else if(vote_end.val() > max.toISOString()){
		alert("투표 종료 시간은 최대 7일 이전입니다.");
		vote_end.val(max.toISOString().substring(0, 17)+'00');
		return false;
	}
	return true;
}

// 투표 종료 시간 변경할 때 검사
$("#vote_end").change( timecheck );

// submit (유효성 검사 포함 (내용이 있는지 없는지))
document.getElementById('btn_save').onclick = function(){
	let title = document.getElementById('tb_title').value;
	let content = document.querySelector('.note-editable').innerHTML
	if(title.trim().length < 2){
		alert('2글자 이상 제목을 입력해 주세요');
	}else if(isEmpty(content).length == 0){
		alert('내용을 입력해 주세요');
	}else{
		let vote_check = true;
		// 투표 유효성 검사
		if($("#vote").css("display") == "block"){
			vote_check = timecheck();
			$("#vote input").each(function(){
				if($.trim($(this).val()) == 0){
					alert('투표 제목 또는 항목을 입력해 주세요.');
					vote_check = false;
					return false; // each 탈출. 함수 전체를 탈출하는거 아님.
				} 
			});
		}
		if(vote_check){
			boardform.tb_content.value = content;
			boardform.submit();
		}
	}
}

// 리셋 버튼
document.getElementById('btn_reset').addEventListener("click", function(){
	boardform.reset();
	$('#summernote').summernote('reset');
	$("#vote").hide();
	$(".new_item").remove();
	$(".del_item").remove();
	$("#vote_content").attr("disabled", true); 
	$("#vote input").attr("disabled", true);
});