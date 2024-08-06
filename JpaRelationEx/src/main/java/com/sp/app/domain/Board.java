package com.sp.app.domain;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "board")
public class Board {
	@Id
	@Column(name = "num", columnDefinition = "NUMBER")
	@SequenceGenerator(name="S_MY_SEQ", sequenceName = "board_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_MY_SEQ")
	private long num;
	
	@Column(name="memberidx", nullable = false, length = 50, updatable = false)
	private Long id;
	
	@Column(nullable = false, length = 500)
	private String subject;
	
	@Column(nullable = false, length = 4000)
	private String content;
	
	// 컬럼명에 _가 있는 경우 findBy~ 등으로 검색이 불가능하므로 다음과 같이 자바 컬럼명을 설정한다.
	@Column(name = "reg_date", nullable = false, columnDefinition = "DATE DEFAULT SYSDATE", 
			updatable = false)
	private LocalDateTime regdate;
	
	@Column(name = "hitcount", nullable = false, columnDefinition = "NUMBER DEFAULT 0",
			insertable = false, updatable = false) // INSERT, UPDATE 제외
	private int hitCount;
	
	@Column(name="savefilename", nullable = true, length = 500)
	private String saveFilename;
	
	@Column(name="originalfilename", nullable = true, length = 500)
	private String originalFilename;

	@ManyToOne					// insertable = false, updatable = false : 중요
	@JoinColumn(name = "memberidx", insertable = false, updatable = false)
	private Member member;
	
	@Transient // 테이블 컬럼에서 제외
	private MultipartFile selectFile; // <input type='file' name='selectFile' ..
	
	@PrePersist // INSERT 전에 호출한다. 
	public void prePersist() {
		this.regdate = this.regdate == null ? 
				LocalDateTime.now() : this.regdate;
	}
}
