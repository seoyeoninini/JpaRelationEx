package com.sp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
	private Long memberIdx;
	private String userId;
	private String userPwd;
	private String userName;
	private int userRole;
	private String register_date;
	private String modify_date;
	
	private String email;
	private String tel;
	private String birth;
	private String zip;
	private String addr1;
	private String addr2;
}
