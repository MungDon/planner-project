package com.planner.oauth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.member.ReqOAuth2MemberAdd;
import com.planner.dto.request.member.ReqOAuth2Signup;
import com.planner.dto.response.member.ResOAuth2MemberLogin;
import com.planner.enums.MemberStatus;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final MemberMapper memberMapper;
	
	/*소셜 회원가입*/
	@Transactional
	public void createMember(OAuth2UserPrincipal principal) {
		
		ReqOAuth2MemberAdd req = ReqOAuth2MemberAdd.builder()
				.member_email(principal.getUsername())
				.member_password(principal.getPassword())
				.member_name(principal.getName())
				.member_status(MemberStatus.NOT_DONE.getCode())
				.oauth_id(principal.getOAuthId())
				.build();
 		memberMapper.createMember(req);
	}
	
	/*소셜회원정보 가져오기*/
	@Transactional(readOnly = true)
	public ResOAuth2MemberLogin findByOAuthId(String oauthId) {
		return memberMapper.findByOAuthID(oauthId);
	}
	
	/*소셜로 받아오지못한 회원정보 저장*/
	@Transactional
	public void fetchAdditionalUserInfo(ReqOAuth2Signup req,OAuth2UserPrincipal principal) {
		req.setOauth_id(principal.getOAuthId());
		req.setMember_status(MemberStatus.BASIC.getCode());
		memberMapper.fetchAdditionalUserInfo(req);
	}
}
