package com.planner.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planner.dto.request.friend.FriendDTO;
import com.planner.dto.request.friend.FriendRequestDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.FriendService;
import com.planner.service.MemberService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend/*")
public class FriendController {

	private final FriendService friendService;
	private final MemberService memberService;
	
//	친구찾기 리스트 Get
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("findFriend")
//	public String findFriend() {
//		
//		return "friend_findFriend";
//	}
	
//	친구추가 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendRequest")
	@ResponseBody
	public String friendRequest(@RequestParam("member_id") Long member_id,
								@UserData ResMemberDetail dtail, Model model) {
		friendService.friendRequest(member_id, dtail.getMember_email());			// 친구신청 void 메서드
		
		Long myid = memberService.findByMemberId(dtail.getMember_email());
		String friendStatus = friendService.friendRequestStatus(member_id, myid);	// 친구신청 상태 찾는 메서드 / (받는 아이디, 보낸 아이디)
		
		return friendStatus;
	}
	
//	(받은)친구신청 리스트 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("receiveList")
	public String receiveList(@UserData ResMemberDetail dtail, Model model) {
		List<FriendRequestDTO> receiveList = friendService.receiveRequestList(dtail.getMember_email());
		int receive_count = friendService.receiveRequestCount(dtail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		model.addAttribute("receiveList", receiveList);
		
		return "friend/friend_receive";
	}
	
//	(보낸)친구신청 리스트 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("sendList")
	public String sendList(@UserData ResMemberDetail dtail, Model model) {
		List<FriendRequestDTO> sendList = friendService.sendRequestList(dtail.getMember_email());
		int receive_count = friendService.receiveRequestCount(dtail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		model.addAttribute("sendList", sendList);
		
		return "friend/friend_send";
	}
	
//	친구 헤더 (받은 친구신청 수)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("friendHeader")
	public String friendHeader(@UserData ResMemberDetail dtail, Model model) {
		int receive_count = friendService.receiveRequestCount(dtail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		
		return "fragments/friend";
	}
	
	
//	친구수락 (친구상태 업데이트) Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendAccept")
//	@ResponseBody
	public String friendAccept(@RequestParam("member_send_id") Long member_send_id,
							   @UserData ResMemberDetail detail, Model model) {
		friendService.friendAccept(detail.getMember_email(), member_send_id);			// 요청 상태 업데이트 메서드
//		friendService.friendAdd(principal, member_send_id);				// 친구 테이블에 추가 메서드
		
//		Long myid = memberService.findByMemberId(principal.getName());
//		String friendStatus = memberService.findByMemberFriendStatus(member_id, myid);
		
		return "redirect:/friend/receiveList";
	}
	
//	친구신청 취소/거절 Post
	@PostMapping("requestDelete")
	@PreAuthorize("isAuthenticated()")
	public String requestDelete(@RequestParam(name = "delete_who", defaultValue = "none") String delete_who,
								FriendRequestDTO friendRequestDTO, @UserData ResMemberDetail detail) {
		Long myId = memberService.findByMemberId(detail.getMember_email());
		if (delete_who.equals("send")) {
			friendService.requestDelete(myId, friendRequestDTO.getMember_send_id());
		}else if (delete_who.equals("receive")) {
			friendService.requestDelete(friendRequestDTO.getMember_receive_id(), myId);
			
			return "redirect:/friend/sendList";
		}
		
		return "redirect:/friend/receiveList";
	}
	
//	(보낸)친구신청 취소 Post
//	@PostMapping("receiveDelete")
//	@PreAuthorize("isAuthenticated()")
//	public String sendDelete(@RequestParam("member_receive_id") Long member_receive_id,
//			Principal principal) {
//		friendService.sendDelete(member_receive_id, principal);
//		
//		return "redirect:/friend/receiveList";
//	}
	
//	친구목록 Get
	@GetMapping("friendList")
	@PreAuthorize("isAuthenticated()")
	public String friendList(@UserData ResMemberDetail detail, Model model) {
		List<FriendDTO> friendList = friendService.friendList(detail.getMember_email());
		int receive_count = friendService.receiveRequestCount(detail.getMember_email());	// 받은 친구신청 수
		model.addAttribute("receive_count", receive_count);
		model.addAttribute("friendList", friendList);
		
		return "friend/friend_friendList";
	}
	
//	친구 닉네임 변경 Post
	@PostMapping("friendName")
	@PreAuthorize("isAuthenticated()")
	public String friendName(FriendDTO friendDTO) {
		friendService.friendNickName(friendDTO);
		
		return "redirect:/friend/friendList";
	}
	
//	친구 메모 변경 Post
//	@PreAuthorize("isAuthenticated()")
//	@PostMapping("friendMemo")
//	public String friendMemo(FriendDTO friendDTO) {
//		friendService.friendMemo(friendDTO);
//		
//		return "redirect:/friend/friendList";
//	}
	
//	친구정보 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendInfo")
	public String friendInfo(FriendDTO frndDTO, RedirectAttributes rttr, @UserData ResMemberDetail detail,
							 @RequestParam(name = "friend_change", defaultValue = "none") String friend_change) {
		if (friend_change.equals("nick")) {
			friendService.friendNickName(frndDTO);
		}else if (friend_change.equals("memo")) {
			friendService.friendMemo(frndDTO);
		}
		
		int receive_count = friendService.receiveRequestCount(detail.getMember_email());	// 받은 친구신청 수
		rttr.addFlashAttribute("receive_count", receive_count);				// rttr 로 보냄
		
		FriendDTO friendDTO = friendService.friendInfo(frndDTO);
		friendDTO.setFriend_status(frndDTO.getFriend_status());		// 정방향 / 역방향 여부를 알려주는 변수
		rttr.addFlashAttribute("friendDTO", friendDTO);
		
		return "redirect:/friend/friendInfo";
	}

//	친구정보 Get
	@PreAuthorize("isAuthenticated()")
	@GetMapping("friendInfo")
	public String friendInfo(@ModelAttribute("friendDTO")FriendDTO friendDTO,
							 @ModelAttribute("receive_count") int receive_count, Model model) {
		model.addAttribute("friendDTO", friendDTO);
		model.addAttribute("receive_count", receive_count);
		
		return "friend/friend_friendInfo";
	}
	
//	친구삭제 Post
	@PreAuthorize("isAuthenticated()")
	@PostMapping("friendDelete")
	public String friendDelete(@RequestParam("friend_id") Long friend_id,
							   @RequestParam("member_my_id") Long member_my_id,
							   @RequestParam("member_friend_id") Long member_friend_id) {
		// 친구테이블(member_my_id) == 친구요청테이블(member_receive_id)
		friendService.friendDelete(friend_id, member_my_id, member_friend_id);	// 친구, 친구요청 삭제 메서드
		
		return "redirect:/friend/friendList";
	}
	
	
//	친구정보 수정 Post
//	@PreAuthorize("isAuthenticated()")
//	@PostMapping("friendInfo")
//	public String friendInfo(FriendDTO friendDTO) {
//		if (friend_change.equals("nick")) {
//			System.out.println("========================");
//			System.out.println(friendDTO.getFriend_status());
//		}
//		friendService.friendNickName(friendDTO);
//		return "redirect:/friend/friendInfo";
//	}
}