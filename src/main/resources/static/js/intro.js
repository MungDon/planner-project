const wrap = document.getElementsByClassName('wrap')[0]; // 보일 영역
const container = document.getElementsByClassName('container');
let page = 0; // 영역 포지션 초기값
const lastPage = container.length - 1; // 마지막 페이지
let isAnimating = false; // 페이지 이동 애니메이션이 진행 중인지 여부

window.addEventListener('wheel', handleScroll, { passive: false });

function handleScroll(e) {
    e.preventDefault();

    if (isAnimating) {
        return; // 애니메이션이 진행 중이면 추가 스크롤 이벤트 무시
    }

    if (e.deltaY > 0) {
        page++;
    } else if (e.deltaY < 0) {
        page--;
    }

    if (page < 0) {
        page = 0;
    } else if (page > lastPage) {
        page = lastPage;
    }

    animatePageTransition();
}

function animatePageTransition() {
    isAnimating = true;
    wrap.style.top = page * -100 + 'vh';

    // 예시로 setTimeout을 사용하여 애니메이션 완료 후 isAnimating을 false로 설정
    setTimeout(() => {
        isAnimating = false;
/*
        // 페이지가 마지막 페이지일 경우 메인 페이지로 이동
        if (page === lastPage) {
            location.href = PAGE_LIST.MAIN_PAGE;
        }
*/
    }, 500); // 애니메이션 지속 시간에 맞춰 설정 (예시로 1초) 현재 .5초
}
