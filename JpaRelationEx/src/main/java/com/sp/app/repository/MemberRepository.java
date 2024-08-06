package com.sp.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.app.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	public Optional<Member> findByUserId(String userId);
}
