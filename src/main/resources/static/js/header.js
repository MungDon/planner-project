$(document).ready(function(){
	$('.menu_btn').click(function(){
		$('.menu_btn').toggleClass('menu_toggle');
		$('.gnb').toggleClass('gnb_toggle');
	});
	
	$('.gnb li').click(function(){
		$(this).find('.sub').slideToggle();
		var t = $(this).find('.sub');
		$('.sub').not(t).slideUp();
	});

	var max_h = 0;
	$(".sub").each(function(){
		var h = parseInt($(this).css("height"));
		if (max_h < h) {
			max_h = h;
		}
	});

	/* 마우스 over */
	$('.gnb').mouseenter(function(){
		var menuHeight = $('#header').outerHeight();
		$('.hd_bg').css({
			'top': menuHeight + 'px',
			height: max_h + 30 + 'px',
		});
		$('#header').addClass('open');
	});
	
	$('.gnb').mouseleave(function(){
		$('.hd_bg').css('height', '0');
		$('#header').removeClass('open');
	});
	
	$('.gnb > li').mouseenter(function(){
		$(this).addClass('active');
		$(this).siblings().removeClass('active');
	});
	$('.gnb > li').mouseleave(function() {
		$(this).removeClass('active');
	});
});