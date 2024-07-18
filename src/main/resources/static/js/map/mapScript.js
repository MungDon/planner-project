function openModal(btn) {
	const form = btn.closest('form');
	const signModal = document.getElementById("signModal");
	const schedule_id = form.querySelector('.schedule_id');

	signModal.style.display = "block";
	relayout();

	if (schedule_id && schedule_id.length != 0) {
		id = schedule_id.value;
	} else {
		id = 0;
	}

	if (schedule_id != null) {
		document.getElementById('insert_place').style.display = 'none';
		document.getElementById('update_place').style.display = 'inline-block';
	} else if (schedule_id == null) {
		document.getElementById('insert_place').style.display = 'inline-block';
		document.getElementById('update_place').style.display = 'none';
	}

}

function closeModal() {
	const signModal = document.getElementById("signModal");
	signModal.style.display = "none";
}

function openMapLikeModal() {
    // 여기서 추가되던지 삭제됬던거 목록들 다보여줄거임!
    const ajaxObj = {
        url: "/updateList",
        method: "get",
        successFn: (response) => {
            if (response.length > 0) {
                 const table = document.getElementById('mapLikeTable');
                 table.innerHTML = '';
                for (let i = 0; i <= response.length; i++) {
                    const item = response[i];
                    const tr = document.createElement('tr');
                    const td1 = document.createElement('td');
                    td1.classList.add('mapLikeBtn'); // 클래스 추가
                    // 버튼 생성 
                    const button = document.createElement('button');
                    button.setAttribute('type', 'button');   // 버튼 속성 추가 
                    button.addEventListener('click', function() {
                        listAdd(this, item.map_number);
                    });
                    button.textContent = '일정추가';
                    // td안에 버튼 넣기 
                    td1.appendChild(button);
                    tr.appendChild(td1);
                    // 체크박스 생성
                    const td2 = document.createElement('td');
                    td2.classList.add('mapLikeTd');   // 클래스 추가
                    const input = document.createElement('input');
                    input.setAttribute('type', 'checkbox');
                    input.setAttribute('name', 'mapLikeListcheckbox');
                    input.setAttribute('class', 'nums');
                    input.setAttribute('value', item.map_number); // th:value 대신 value 사용
                    // td에 넣기 
                    td2.appendChild(input);
                    tr.appendChild(td2);
                    // place 생성    
                    const td3 = document.createElement('td');
                    td3.textContent = item.map_title; // 제목 설정
                    td3.setAttribute('th:value', item.map_title);
                    td3.id = "mapLikePlace";
                    tr.appendChild(td3);
                    // address 생성 
                    const td4 = document.createElement('td');
                    td4.textContent = item.map_address; // 주소 설정
                    td4.setAttribute('th:value', item.map_address);
                    td4.id = "mapLikeAddress";
                    tr.appendChild(td4);
                    tr.classList.add('mapLikeTr');
                    table.appendChild(tr); // table에 tr 추가
	                closeModal(); // 모달 닫기 함수 호출
	                const mapLikeModal = document.querySelector(".MpaLikeChkModal");
	                console.log(mapLikeModal);
	                mapLikeModal.style.display = "block"; // 모달 열기
                }
            } else {
                swalCall("실패", "즐겨찾기 목록이 없습니다");
                console.log("실패로옴");
            }
        }
    };
    ajaxCall(ajaxObj);
}

function closeMapLikeModal() {
	const mapLikeModal = document.getElementById("MpaLikeModal");
	mapLikeModal.style.display = "none";
}

