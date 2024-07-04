const monthNames = [
	'January', 'February', 'March', 'April', 'May', 'June',
	'July', 'August', 'September', 'October', 'November', 'December'
];

let currentMonth;
let currentYear;
let local_date_start;		// datetime-local 에 들어갈 값을 저장해둔것임.
let local_date_end;
let clickedDate;				// 클릭한 날짜의 값을 저장해 두는것임.
let id;									// form 의 번호임.

const header = $("meta[name='_csrf_header']").attr('content');
const token = $("meta[name='_csrf']").attr('content');

document.addEventListener("DOMContentLoaded", function() {
	var flashMessage = document.getElementById("flashMessage");
	if (flashMessage && flashMessage.textContent.trim() !== '') {
		alert(flashMessage.textContent);
	}
});


// 달력 생성 함수
function createCalendar(month, year) {

	// HTML 요소 가져오기
	const monthName = document.querySelector('.month-name');
	const weekdays = document.querySelector('.weekdays');
	const days = document.querySelector('.days');

	// 현재 달과 연도를 표시함.		ex) june 2024
	monthName.textContent = `${monthNames[month - 1]} ${year}`;
	monthName.dataset.month = month;
	monthName.dataset.year = year;
	// 요일 추가 
	const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
	weekdays.innerHTML = daysOfWeek.map(day => `<div>${day}</div>`).join('');
	// .innerHTML : HTML 코드를 자바 스크립트 코드에서 변경 가능하게함.

	// 날짜 추가 .Date : 날짜 생성	.getDate : 가져오는거.
	const firstDayOfMonth = new Date(year, month - 1, 1);
	const lastDayOfMonth = new Date(year, month, 0);
	const lastDateOfMonth = lastDayOfMonth.getDate();
	const firstDayOfWeek = firstDayOfMonth.getDay();

	// 이전 달에서의 공백 추가
	days.innerHTML = '';
	for (let i = 0; i < firstDayOfWeek; i++) {
		const blank = document.createElement('div');
		blank.classList.add('day', 'blank');
		days.appendChild(blank);
	}

	// 현재 달의 날짜 추가
	for (let i = 1; i <= lastDateOfMonth; i++) {
		const day = document.createElement('div');
		day.classList.add('day');
		day.textContent = i;
		days.appendChild(day);

		// 오늘 
		const currentDate = new Date();
		if (i === currentDate.getDate() && month === currentDate.getMonth() + 1 && year === currentDate.getFullYear()) {
			day.classList.add('today');
		}
		
	}
	$(document).off("click");

	// 날짜 클릭하면.
	function handleDayClick() {
		let year = $(".month-name").data("year");
		let month = $(".month-name").data("month");
		clickedDate = year + String(month).padStart(2, '0') + String($(this).text()).padStart(2, '0');
		var clickDate = new Date(year, month - 1, $(this).text());
		clickDate.setDate(clickDate.getDate() + 1);
		var clickDateStr = clickDate.toISOString().slice(0, 16);

		var clickDateSec = new Date(year, month - 1, $(this).text());
		clickDateSec.setDate(clickDateSec.getDate() + 1);
		clickDateSec.setMinutes(clickDateSec.getMinutes() + 30);
		var clickDateStr2 = clickDateSec.toISOString().slice(0, 16);

		local_date_start = clickDate.toISOString().slice(0, 16);
		local_date_end = clickDateSec.toISOString().slice(0, 16);

		$.ajax({
			url: "schedule",
			type: "get",
			data: { "date": clickedDate },
			success: function(html) {

				$(".schedule").empty();
				$(".schedule").append(html);

				// 원래 right 에서 처리 했지만 . 그렇게 되면 오류 뜸.  -> 페이지 로딩시에 date를 받아오지 않기 때문에 일어난 일임.
				// 따라서 클릭해서 실행 했을 때 ? Date의 값을 받아오면 오류가 생기지 않음. 
				// 여기 아래는 dateTimeLocal 관련한 스크립트들 
				let dateElement = document.querySelector('.dateTimeLocal');
				let date = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
				dateElement.value = clickDateStr;
				dateElement.setAttribute("min", date);

				let secondDateElement = document.querySelector('.secondDateTimeLocal');
				secondDateElement.value = clickDateStr2;
				secondDateElement.setAttribute("min", clickDateStr);
			}
		});
	}
	$(document).on("click", ".day", handleDayClick);
}//createCalendar 끝

// 이전 달을 보여주는 함수
function previousMonth() {
	currentMonth--;
	if (currentMonth < 1) {
		currentMonth = 12;
		currentYear--;
	}
	updateCalendar();
}

// 다음 달을 보여주는 함수
function nextMonth() {
	currentMonth++;
	if (currentMonth > 12) {
		currentMonth = 1;
		currentYear++;
	}
	updateCalendar();
}

// 캘린더 업데이트 함수
function updateCalendar() {
	createCalendar(currentMonth, currentYear); // 요일과 날짜 업데이트
}

// 페이지 로드 시 실행되는 함수
window.onload = function() {
	const today = new Date();
	currentMonth = today.getMonth() + 1;
	currentYear = today.getFullYear();

	if (currentMonth.length == 1) {
		currentMonth = '0' + currentMonth;
	}
	createCalendar(currentMonth, currentYear); // 현재 달력 생성
};

//========================================================================================== 이 아래는 right 임.

// 여기는 버튼 클릭시 글작성 div 왔다 갔다 하는 스크립트
function btnClick() {
	const mydiv = document.getElementById('my-div');

	if (mydiv.style.display !== 'block') {
		mydiv.style.display = 'block';
	} else {
		mydiv.style.display = 'none';
	}
}

// 수정하기 버튼 누르면 read only가 풀리는것.
function enableInputs(button) {
	const form = button.closest('form');
	form.querySelector('#edtScheduleStart').readOnly = false;
	form.querySelector('#edtScheduleEnd').readOnly = false;
	form.querySelector('.schedule_title').readOnly = false;
	form.querySelector('.schedule_content').readOnly = false;
	form.querySelector('.submit_btn').style.display = 'inline-block';
	form.querySelector('.none_btn').style.display = 'inline-block';
	button.style.display = 'none';

	// edt_mapPlace 요소 가져오기
	const edt_place = form.querySelector('.mapPlace');
	if (edt_place.value.trim().length != 0) {
		const trimmedValue = edt_place.value.trim(); // 값 가져오기 및 공백 제거
		if (trimmedValue !== '') { // 값이 비어 있지 않은 경우
			form.querySelector('.edt_place').style.display = 'inline-block';
		}
	} else {
		// edt_place가 null인 경우에 대한 처리
		form.querySelector('.add_place').style.display = 'inline-block';
	}
}

// 수정하기 버튼 누르면 보이는 리셋 버튼 누르면 실행되는 함수.
function resetForm(button) {
	const form = button.closest('form');
	setTimeout(() => {
		form.querySelector('#edtScheduleStart').readOnly = true;
		form.querySelector('#edtScheduleEnd').readOnly = true;
		form.querySelector('.schedule_title').readOnly = true;
		form.querySelector('.schedule_content').readOnly = true;
		form.querySelector('.edit_btn').style.display = 'inline-block';
		form.querySelector('.submit_btn').style.display = 'none';
		form.querySelector('.none_btn').style.display = 'none';
	}, 0);
}

// 글 작성시 
function writeSchedule() {
	const firstDate = document.querySelector('.dateTimeLocal');
	const secondDate = document.querySelector('.secondDateTimeLocal');
	const writeContent = document.getElementById('schedule_content');
	const writeTitle = document.getElementById('schedule_title');

	// 공백 잡아내는 역할
	function isEmpty(tag) {
		if (tag.value.trim().length == 0) {
			return true;
		} else {
			return false;
		}
	}
	// required 처럼 사용할 수 있음.
	if (isEmpty(firstDate) || isEmpty(secondDate) || isEmpty(writeContent) || isEmpty(writeTitle)) {
		alert('모든 값을 입력해주세요!');
		return;
	}

	if (dateCheck(firstDate.value, secondDate.value)) {
		return;
	}

	$.ajax({
		url: 'schedule',
		type: 'POST',
		data: $("#form").serialize(),
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success: function() {
			// 첫 번째 AJAX 요청 성공 시 실행되는 부분
			$(".schedule").load(window.location.href + "");

			scheduleAjax();
		},
		error: function(xhr, status, error) {
			console.error("글 작성시에 오류 발생", status, error);
		}
	});
}

// 삭제 버튼
function deleteScheduel(btn) {
	const schedule_id = btn.value;
	$.ajax({
		url: 'schedule/del',
		type: 'delete',
		data: { schedule_id: schedule_id },
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success: function() {
			$(".schedule").load(window.location.href + "");
			scheduleAjax();
		}
	});
}

// 수정
function editSchedule(btn) {
	const schedule_id = btn.value;
	var form = $(btn).closest('form'); // 클릭된 버튼의 부모 form 요소 선택
	var formData = form.serialize(); // 해당 form의 데이터 serialize

	var start = form.find('#edtScheduleStart').val();		// 값 찾아서 꺼내옴.
	var end = form.find('#edtScheduleEnd').val();

	if (dateCheck(start, end)) {
		return;
	}

	$.ajax({
		url: 'schedule/edt',
		type: 'post',
		data: formData,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success: function() {
			$(".schedule").load(window.location.href + "");
			scheduleAjax();
		}
	});
}

// 처리 이후 페이지 새로고침 하는 ajax;
function scheduleAjax() {
	$.ajax({
		url: "schedule",
		type: "GET",
		data: { "date": clickedDate },
		success: function(html) {
			$(".schedule").empty();
			$(".schedule").append(html);

			let dateElement = document.querySelector('.dateTimeLocal');
			let date = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
			dateElement.value = local_date_start;
			dateElement.setAttribute("min", date);

			let secondDateElement = document.querySelector('.secondDateTimeLocal');
			secondDateElement.value = local_date_end;
			secondDateElement.setAttribute("min", date);
		},
		error: function(xhr, status, error) {
			console.error("삭제 후 이동 오류", status, error);
		}
	});
}

// 시간 유효성 검사.
function dateCheck(start, end) {
	if (new Date(start) > new Date(end)) {
		alert('시작 시간이 종료 시간보다 늦을 수 없습니다.');
		return ture; // 유효성 검사 실패 시 함수 종료
	} else {
		return false;
	}
}
