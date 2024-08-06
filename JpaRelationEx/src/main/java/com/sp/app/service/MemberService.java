package com.sp.app.service;

import com.sp.app.dto.SessionInfo;

public interface MemberService {
	public SessionInfo signIn(String userId, String userPwd);

}
