const modal = document.getElementById('modal');
const check_delete = document.getElementById('check_delete');
const modal_close = document.getElementById('modal_close');
const team_id = document.querySelector('meta[name="team_id"]').getAttribute('content');
const team_member_id = document.querySelector('meta[name="team_member_id"]').getAttribute('content');
const team_board_id = document.querySelector('meta[name="team_board_id"]').getAttribute('content');
const vote_id = document.querySelector('meta[name="vote_id"]').getAttribute('content');
const header = $('meta[name="_csrf_header"]').attr("content");
const token = $('meta[name="_csrf_token"]').attr("content");
// 회원 정보 링크
$(document).on("click", ".team_member", function(){
	location.href = '/team/member/info?team_id='+team_id+'&team_member_id='+$(this).data("team_member_id");
})

if(check_delete !== null){
	// 글삭제 modal창 on
	check_delete.addEventListener('click', function(){
		modal.style.display = 'block';
	})
	// 글삭제 modal창 off
	modal_close.addEventListener('click', function(){
		modal.style.display = 'none';
	})
}

/*
	$("선택자").on("click", function(){});
	$(document).on("click", "선택자", function);
	두개는 서로 다름
	on(); 한 이후에 동적으로 객체가 생성되었을 때
	선택자로 선택한 것은 click 이벤트가 적용이 안되고
	document로 시작한 것은 똑같이 적용됨.
*/
// 글 삭제
$("#board_delete").on("click", function(){
	let data = $(this).data();
	data._csrf = token;
	$.ajax({
		type:"post",
		url:"/team/board/delete",
		data: data,
		success: function(result){
			window.location = result;
		},
	});
});
// 댓글 불러오기
function getReply(){
	$.ajax({
		type:"get",
		url:"/reply/list",
		data:{
			team_id:team_id,
			team_board_id:team_board_id
		},
		success:function(result){
			$("#reply_list").html(result);
		}
	})
}
// 댓글 삭제 버튼
$(document).on("click",".re-del-btn", function(){
	let data = $(this).data();
	if(confirm("댓글을 삭제하시겠습니까?")){
		$.ajax({
			type:"delete",
			url:"/reply/delete",
			data:data,
			beforeSend:function(xhr){
				xhr.setRequestHeader(header, token);
			},
			success:function(){
				$("#"+data.reply_id).remove();
				$("#reply_count").text($("#reply_count").text()-1);
			}
		});
	}
});
// 댓글 수정 버튼
$(document).on("click", ".re-mod-btn", function(){
	$(".rereply").remove();
	$(".modify").remove();
	$(".reply").css("display", "block");
	let reply_id = $(this).data("reply_id");
	let reply_content = $("#"+reply_id+" .reply_content").text();
	let modify_div = $('<div class="reply_form modify"><textarea>'+reply_content+'</textarea></div>');
	modify_div.append('<button type="button" class="modify-btn">수정</button><button type="button" class="cancel-btn">취소</button>');
	modify_div.insertAfter($("#"+reply_id));
	$("#"+reply_id).css('display', 'none');
	
	// 수정 취소 버튼
	$(".cancel-btn").on("click", function(){
		$("#"+reply_id).css('display', 'block');
		modify_div.remove();
	});
	
	// 수정 확인 버튼
	$(".modify-btn").on("click", function(){
		let modify_area = $("#"+reply_id+"+.modify textarea");
		let data = {reply_id:reply_id, reply_content:modify_area.val()};
		if(data.reply_content.trim().length == 0){
			alert("댓글 내용을 입력해 주세요.");
		}else{
			$.ajax({
				type:"patch",
				data:data,
				url:"/reply/modify",
				beforeSend:function(xhr){
					xhr.setRequestHeader(header, token);
				},
				success:function(){
					$("#"+reply_id).css('display', 'block');
					$("#"+reply_id+" .reply_content").text(data.reply_content);
					modify_div.remove();
				},
				error:function(){
					alert("댓글 수정 실패");
				}
			});
		}
	});
});
// 대댓글 작성 폼 생성
$(document).on("click", ".reply_content", function(){
	$(".modify").remove();
	$(".rereply").remove();
	$(".reply").css("display", "block");
	let reply_group = $(this).data("reply_group");
	let rereply_div = $('<div class="reply_form rereply"><textarea name="reply_content"></textarea></div>');
	rereply_div.append('<input type="hidden" name="reply_group" value='+reply_group+'>');
	rereply_div.append('<button type="button" class="rereply-insert">등록</button>');
	rereply_div.append('<button type="button" class="rereply-cancel">취소</button>');
	rereply_div.insertAfter(this);
	
});
// 대댓글 작성 취소
$(document).on("click", ".rereply-cancel", function(){
	$(".rereply").remove();
});
// 대댓글 작성 저장
$(document).on("click", ".rereply-insert", function(){
   let data = {
      reply_group:$(".rereply input[name='reply_group']").val(),
      reply_content:$(".rereply textarea[name='reply_content']").val(),
      team_board_id:team_board_id,
      team_id:team_id
   };
	$.ajax({
		type:"post",
		url:"/reply/insert",
		data:data,
		beforeSend:function(xhr){
			xhr.setRequestHeader(header, token);
		},
		success:function(){
			getReply();
			$(".rereply").remove();
		},
		error:function(){
			alert("댓글 등록을 실패했습니다.");
		}
	});
});
// 댓글 최초 로딩
$(function(){
	getReply();
});
// 댓글 새로고침 (수동)
$("#reply_refresh").on("click", function(){
	getReply();
});
// 댓글 등록
$("#reply_btn").on("click", function(){
	if($("#reply_content").val().trim().length == 0){
		alert("댓글 내용을 입력해 주세요");
	}else{
		let data = $("#reply_form").serializeArray();
		$.ajax({
			type:"post",
			url:"/reply/insert",
			data:data,
			beforeSend:function(xhr){
				xhr.setRequestHeader(header, token);
			},
			success:function(){
				getReply();
				$("#reply_form").each(function(){this.reset();})
			},
			error:function(){
				alert("댓글 등록을 실패했습니다.");
			}
		})
	};
});
// 투표 출력
function getVote(){
	$.ajax({
		type:'POST',
		url:'/vote/info',
		data:{'vote_id':vote_id, 'team_id':team_id},
		beforeSend:function(xhr){
			xhr.setRequestHeader(header, token);
		},
		success:function(result){
			$("#vote").html(result);
		}
	});
}
// 투표 최초 로딩
if(vote_id != null){
	getVote();
}
// 투표하기
$(document).on("click", "#do_vote", function(){
	$("#do_vote").attr("disabled", true);
	$.ajax({
		type:'POST',
		url:'/vote/memberinsert',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		data:JSON.stringify({
			'vote_id':vote_id,
			'vote_item_id':$("input[name='vote_item_id']:checked").val(),
			'team_member_id':team_member_id,
			'vote_end':$("#vote_end").val()
		}),
		beforeSend:function(xhr){
			xhr.setRequestHeader(header, token);
		},
		success:function(result){
			if(result == -1){
				alert('투표가 마감되었습니다.');
			}else if(result == -2){
				alert('중복 투표는 불가능합니다.');
			}
			getVote();
		}
	});
});
// 투표하기, 투표결과 토글
$(document).on("click", ".toggle_result", function(){
	$("#vote_items").toggle();
	$("#vote_result").toggle();
});