const boardform = document.getElementById('boardform');
const modal = document.getElementById('modal');
const modal_close = document.getElementById('modal_close');
const so = document.getElementById('so');
const cal_title = document.querySelector('input[name="cal_title"]');
const cal_date = document.querySelector('input[name="cal_date"]');
const cal_search = document.getElementById('cal_search');
// 글삭제 modal창 on
function calendar_btn(){
	modal.style.display = 'block';
}
// 글삭제 modal창 off
modal_close.addEventListener('click', function(){
	modal.style.display = 'none';
})

// 에디터 일정 버튼
var schedule_btn = function (context) {
	var ui = $.summernote.ui;

	// create button
	var button = ui.button({
		contents: '<i class="fa fa-child"/> 일정',
		click: function () {
			context.invoke(calendar_btn());
		}
	});
	return button.render();
}

// 검색 옵션 바꿀때.
so.addEventListener("change", function(){
	if(so.value == 'T'){
		cal_title.style.display = 'inline-block';
		cal_date.style.display = 'none';
	}else{
		cal_title.style.display = 'none';
		cal_date.style.display = 'inline-block';
	}
});

// 일정 검색
function cal_search_function(){
	let data = {};
	if(so.value == 'T'){ // 제목 검색일 때
		if(cal_title.value.trim().length < 2){
			alert('검색은 최소 2글자 이상입니다.');
			return;
		}
		data = {
			team_id:boardform.team_id.value,
			so:so.value,
			cal_title:cal_title.value
		}
	}else{ // 시작, 종료 날짜 검색일 때
		if(cal_date.value.length == 0){
			alert('날짜를 입력 해주세요.');
			return;
		}
		data = {
			team_id:boardform.team_id.value,
			so:so.value,
			cal_date:cal_date.value
		}
	}
	$.ajax({
		type:'get',
		url:'/team/planner/search',
		data : data,
		success:function(result){
			$("#modal_result").html(result);
		}
	});
}
// 일정 검색 버튼
cal_search.addEventListener("click", cal_search_function);
// 일정 제목 검색일 때 엔터눌러서 검색
cal_title.addEventListener("keydown", function(e){
	if(e.keyCode == 13){
		cal_search_function();
	}
});

// scSearch 검색 후 추가 버튼
$(document).on("click", ".cal_add", function(){
	boardform.schedule_id.disabled = false;
	boardform.schedule_id.value = $(this).closest("tr").data("scid")
	$("#cal_print").html(
		'<span>선택된 일정</span>' + 
		'<button type="button" id="cal_del"> X </button> </br>'+
		'<span>'+$(this).closest("tr").children("td:eq(0)").text()+'</span>' + 
		'<p class="float-right">'+$(this).closest("tr").children("td:eq(2)").html()+'</p>'+
		'<pre>'+$(this).closest("tr").children("td:eq(1)").text()+'</pre>'
	);
	$('#cal_print').show();
	modal.style.display = 'none';
});

// 추가된 일정 제거
$(document).on("click", '#cal_del', function(){
	$('#cal_print').hide();
	$('#cal_print').html('');
	boardform.schedule_id.disabled = true;
});

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
// 에디터 투표 버튼
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
	let min = new Date(Date.now() + 35700000); // 현재시간 + 9시간(UTC+9) + 55분
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

// submit (유효성 검사 포함)
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
			$("#vote input").each(function(){ // 제목, 항목 비어있으면 안됨
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
	$("#modal_result").html('');
	$('#cal_print').hide();
	$('#cal_print').html('');
	so.value='T';
	cal_title.style.display = 'inline-block';
	cal_date.style.display = 'none';
	cal_title.value = '';
	cal_date.value = '';
	boardform.schedule_id.disabled = true;
});