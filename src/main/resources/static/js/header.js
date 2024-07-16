// 상단 메뉴 관련
$('.header > .header-inner > .top-menu-bar > ul > li').mouseenter(function(){
    $(this).addClass('active');
    $(this).find('> a').addClass('active');
});

$('.header > .header-inner > .top-menu-bar > ul > li').mouseleave(function(){
    $(this).removeClass('active');
    $(this).find('> a').removeClass('active');
});

// 상단 2차 메뉴 배경
$('.header > .header-inner > .top-menu-bar > ul > .menu-1').mouseenter(function(){
    $('.sub-menu-bg').css('height','62px');
});
$('.header > .header-inner > .top-menu-bar > ul > .menu-1').mouseleave(function(){
    $('.sub-menu-bg').css('height','');
});

$('.header > .header-inner > .top-menu-bar > ul > .menu-2').mouseenter(function(){
    $('.sub-menu-bg').css('height','128px');
});
$('.header > .header-inner > .top-menu-bar > ul > .menu-2').mouseleave(function(){
    $('.sub-menu-bg').css('height','');
});

$('.header > .header-inner > .top-menu-bar > ul > .menu-3').mouseenter(function(){
    $('.sub-menu-bg').css('height','164px');
});
$('.header > .header-inner > .top-menu-bar > ul > .menu-3').mouseleave(function(){
    $('.sub-menu-bg').css('height','');
});

$('.header > .header-inner > .top-menu-bar > ul > .menu-4').mouseenter(function(){
    $('.sub-menu-bg').css('height','94px');
});
$('.header > .header-inner > .top-menu-bar > ul > .menu-4').mouseleave(function(){
    $('.sub-menu-bg').css('height','');
});

$('.header > .header-inner > .top-menu-bar > ul > .menu-5').mouseenter(function(){
    $('.sub-menu-bg').css('height','0px');
});
$('.header > .header-inner > .top-menu-bar > ul > .menu-5').mouseleave(function(){
    $('.sub-menu-bg').css('height','');
});

// 모바일 메뉴 버튼
$('.m-header > .m-menu-btn').click(function(){
    if ( $(this).hasClass('active') ){
        $('html').removeClass('m-menu-scroll');
        
        $(this).removeClass('active');
        $(this).siblings('.mobile-mask').css('height','');
        $(this).siblings('.m-menu-list').removeClass('active');
        $('.m-header .m-menu-list').css('display', 'none');
    }
    else {
        $('html').addClass('m-menu-scroll');
        
        $('.m-header > .m-search-on').removeClass('active');
        
        $(this).siblings('.m-menu-list').find(' > ul > li.active').removeClass('active');
        $(this).siblings('.m-menu-list').find(' > ul > li > ul').slideUp(0);
        
        $(this).addClass('active');
        $(this).siblings('.mobile-mask').css('height','100%');
        $(this).siblings('.m-menu-list').addClass('active');
        $('.m-header .m-menu-list').css('display', 'block');
    };
});

// 모바일 메뉴 리스트
$('.m-header > .m-menu-list > ul > li').click(function(){
    if ( $(this).hasClass('active') ) {
        $(this).find(' > ul').slideUp(300);
        $(this).removeClass('active');
    }
    else {
        $(this).siblings('li.active').removeClass('active');
        $(this).siblings().find(' > ul').slideUp(300);
        $(this).find(' > ul').slideDown(300);
        $(this).addClass('active');
    }
});

$('.m-header > .m-menu-list > ul > li > ul').click(function(e){
    e.stopPropagation();
});

/* 가로스크롤 제거 */
document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const mHeader = document.querySelector('.m-header');
    
    if (mHeader.scrollWidth <= window.innerWidth) {
        body.style.overflowX = 'hidden';
    }
    
    window.addEventListener('resize', () => {
        if (mHeader.scrollWidth <= window.innerWidth) {
            body.style.overflowX = 'hidden';
        } else {
            body.style.overflowX = 'auto';
        }
    });
});