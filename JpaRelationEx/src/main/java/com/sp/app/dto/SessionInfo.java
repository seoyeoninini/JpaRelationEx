package com.sp.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SessionInfo {
	private Long memberIdx;
	private String userId;
	private String userName;
	private int userRole;
}
