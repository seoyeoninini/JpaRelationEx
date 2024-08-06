package com.sp.app.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
	@Id
	@Column(name = "memberidx")
	@SequenceGenerator(name = "S_MY_SEQ", sequenceName = "member_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_MY_SEQ")
	private Long id;
	
	@Column(name = "userid", unique = true, nullable = false, length = 50)
	private String userId;
	
	@Column(name = "userpwd", nullable = false, length = 100)
	private String userPwd;
	
	@Column(name = "username", nullable = false, length = 30)
	private String userName;
	
	@Column(name = "userrole", nullable = false, length = 3)
	private int UserRole;
	
	@Column(nullable = false, columnDefinition = "DATE DEFAULT SYSDATE", insertable = false, updatable = false)
	private LocalDateTime register_date;
	
	@Column(nullable = false, columnDefinition = "DATE DEFAULT SYSDATE", insertable = false )
	private LocalDateTime modify_date;
}
