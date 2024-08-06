package com.sp.app.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sp.app.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	// findBy컬럼명Containing() => 컨테이닝(Containing) : LIKE 비교
	// 복수개의 And 조건 :   findByField1AndField2(String field1, String field2)
	public Page<Board> findBySubjectContainingOrContentContaining(String subject, String content, Pageable pageable);
	public Page<Board> findBySubjectContaining(String kwd, Pageable pageable);
	public Page<Board> findByContentContaining(String kwd, Pageable pageable);
	
	public Page<Board> findByMember_UserNameContaining(String kwd, Pageable pageable);
	
	public Page<Board> findByRegdateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
	
	@Transactional	// 필수
	@Modifying		// update, insert 시
	@Query(value = "UPDATE board SET hitCount=hitCount+1 WHERE num = :num",
			nativeQuery = true) // false:JPQL, true:SQL
	public int updateHitCount(@Param("num") long num);
	
	@Query(value = "SELECT * FROM board WHERE num>:num ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrev(@Param("num") long num);
	@Query(value = "SELECT b.* FROM board b JOIN member m ON b.userId = m.userId WHERE num>:num AND userName LIKE '%'||:kwd||'%' ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrevName(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num>:num AND TO_CHAR(reg_date, 'YYYYMMDD')=:kwd ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrevDate(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num>:num AND (subject LIKE '%'||:kwd||'%') ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrevSubject(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num>:num AND (content LIKE '%'||:kwd||'%') ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrevContent(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num>:num AND (subject LIKE '%'||:kwd||'%' OR content LIKE '%'||:kwd||'%') ORDER BY num ASC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByPrevAll(@Param("num") long num, @Param("kwd") String kwd);
	
	@Query(value = "SELECT * FROM board WHERE num<:num ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNext(@Param("num") long num);
	@Query(value = "SELECT b.* FROM board b JOIN member m ON b.userId = m.userId WHERE num<:num AND userName LIKE '%'||:kwd||'%' ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNextName(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num<:num AND TO_CHAR(reg_date, 'YYYYMMDD')=:kwd ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNextDate(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num<:num AND (subject LIKE '%'||:kwd||'%') ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNextSubject(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num<:num AND (content LIKE '%'||:kwd||'%') ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNextContent(@Param("num") long num, @Param("kwd") String kwd);
	@Query(value = "SELECT * FROM board WHERE num<:num AND (subject LIKE '%'||:kwd||'%' OR content LIKE '%'||:kwd||'%') ORDER BY num DESC FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
	public Board findByNextAll(@Param("num") long num, @Param("kwd") String kwd);
	
}
