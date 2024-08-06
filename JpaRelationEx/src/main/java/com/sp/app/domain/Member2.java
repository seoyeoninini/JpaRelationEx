package com.sp.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자없는 생성자를 만들지만 인자없는 생성자는 다른 패키지에 접근 불가
@AllArgsConstructor
@Builder
public class Member2 {
	@Id
	@Column(name = "userid", unique = true, nullable = false, length = 50)
	private String userId;
	
	private String email;
	
	private String tel;
	
	private String birth;
	
	private String zip;
	
	private String addr1;
	
	private String addr2;
	
	@OneToOne
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	private Member member;
	
}
