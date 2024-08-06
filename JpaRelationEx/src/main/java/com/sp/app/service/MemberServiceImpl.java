package com.sp.app.service;

import org.springframework.stereotype.Service;

import com.sp.app.domain.Member;
import com.sp.app.dto.SessionInfo;
import com.sp.app.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	
	@Override
	public SessionInfo signIn(String userId, String userPwd) {
		SessionInfo dto = null;
		
		try {
			Member entity = memberRepository.findByUserId(userId)
					.orElseThrow(() -> new RuntimeException("등록된 회원이 아닙니다."));
			
			if(entity.getUserPwd().equals(userPwd)) {
				dto = SessionInfo.builder()
						.memberIdx(entity.getId())
						.userId(entity.getUserId())
						.userName(entity.getUserName())
						.userRole(entity.getUserRole())
						.build();
			}
			
		} catch (Exception e) {
		}
		
		return dto;
	}


}
