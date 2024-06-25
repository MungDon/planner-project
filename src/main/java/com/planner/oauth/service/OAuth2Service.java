package com.planner.oauth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.member.ReqOAuth2MemberAdd;
import com.planner.dto.request.member.ReqOAuth2Signup;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberRole;
import com.planner.enums.MemberStatus;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.mapper.MemberMapper;
import com.planner.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final MemberMapper memberMapper;
	private final MemberService memberService;
	
	/*소셜 회원가입*/
	@Transactional
	public void createMember(OAuth2UserPrincipal principal) {
		
		ReqOAuth2MemberAdd req = ReqOAuth2MemberAdd.builder()
				.member_email(principal.getUsername())
				.member_password(principal.getPassword())
				.member_name(principal.getName())
				.member_status(MemberStatus.NOT_DONE.getCode())
				.oauth_id(principal.getOAuthId())
				.oauth_type(principal.getType())
				.member_role(principal.getRole())
				.build();
 		memberMapper.createMember(req);
	}
	
	/*소셜회원정보 가져오기*/
	@Transactional(readOnly = true)
	public ResMemberDetail findByOAuthId(String oauthId) {
		return memberMapper.findByOAuthID(oauthId);
	}
	
	/*소셜로 받아오지못한 회원정보 저장*/
	@Transactional
	public void fetchAdditionalUserInfo(ReqOAuth2Signup req, OAuth2UserPrincipal principal) {
		if(req.getMember_email() !=null) {
			boolean isMember = memberService.isMember(req.getMember_email());
		
			if(isMember) {
				throw new CustomException(ErrorCode.ID_DUPLICATE);// 이메일(아이디 중복)에 대한 커스텀예외
			}
		}
		// 이미 있는 계정(이메일)이면 예외 발생
		req.setOauth_id(principal.getOAuthId());
		req.setMember_status(MemberStatus.BASIC.getCode());
		memberMapper.fetchAdditionalUserInfo(req);
	}
}
