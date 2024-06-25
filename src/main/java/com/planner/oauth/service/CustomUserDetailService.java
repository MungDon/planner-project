package com.planner.oauth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.member.MemberDTO;
import com.planner.enums.MemberRole;
import com.planner.enums.MemberStatus;
import com.planner.mapper.MemberMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String member_email) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        //이메일 체크
        MemberDTO member = memberMapper.findByUser(member_email);
        if(member != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(MemberRole.USER.getType()));
            User user =  new User(member.getMember_email(), member.getMember_password(), grantedAuthorities);
            return user;
        } else {
            // DB에 정보가 존재하지 않으므로 exception 호출
            throw new UsernameNotFoundException("user doesn't exist, email : " + member_email);
        }
    }
}