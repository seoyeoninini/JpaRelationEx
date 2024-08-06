package com.sp.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sp.app.common.FileManager;
import com.sp.app.domain.Board;
import com.sp.app.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;
	private final FileManager fileManager;
	
	@Override
	public void insertBoard(Board entity, String pathname) throws Exception {
		try {
			String saveFilename = fileManager.doFileUpload(entity.getSelectFile(), pathname);
			if (saveFilename != null) {
				entity.setSaveFilename(saveFilename);
				entity.setOriginalFilename(entity.getSelectFile().getOriginalFilename());
			}
			
			boardRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Page<Board> listPage(String schType, String kwd, int current_page, int size) {
		Page<Board> page = null;
		
		try {
			Pageable pageable = PageRequest.of(current_page-1, size, 
					Sort.by(Sort.Direction.DESC, "num"));
			
			if(kwd.length() == 0) {
				page = boardRepository.findAll(pageable);
			} else if(schType.equals("all")) {
				page = boardRepository.findBySubjectContainingOrContentContaining(kwd, kwd, pageable);
			} else if(schType.equals("userName")) {
				page = boardRepository.findByMember_UserNameContaining(kwd, pageable);
			} else if(schType.equals("reg_date")) {
				// 어제 0시0분
				// LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
				// 오늘 0시0분
				// LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
				
				String s = kwd.replaceAll("(\\-|\\/|\\.)", "");
				if(s.matches("\\d{8}")) {
					DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMdd");
					LocalDate date = LocalDate.parse(s, f);
					LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
					LocalDateTime end = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
					page = boardRepository.findByRegdateBetween(start, end, pageable);
				}
			} else if(schType.equals("subject")) {
				page = boardRepository.findBySubjectContaining(kwd, pageable);
			} else if(schType.equals("content")) {
				page = boardRepository.findByContentContaining(kwd, pageable);
			}
		} catch (IllegalArgumentException e) {
			// 게시글이 존재하지 않는 경우 발생하는 예외
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return page;
	}

	@Override
	public Board findById(long num) {
		Board dto = null;
		try {
			Optional<Board> board = boardRepository.findById(num);
			dto = board.get();
			
		} catch (NoSuchElementException e) {
			// 데이터가 없는 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public void updateHitCount(long num) throws Exception {
		try {
			boardRepository.updateHitCount(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Board findByPrev(String schType, String kwd, long num) {
		Board dto = null;
		
		try {
			if(kwd.length()==0) {
				dto = boardRepository.findByPrev(num);
			} else if(schType.equals("userName")) {
				dto = boardRepository.findByPrevName(num, kwd);
			} else if(schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				dto = boardRepository.findByPrevDate(num, kwd);
			} else if(schType.equals("subject")) {
				dto = boardRepository.findByPrevSubject(num, kwd);
			} else if(schType.equals("content")) {
				dto = boardRepository.findByPrevContent(num, kwd);
			} else if(schType.equals("all")) {
				dto = boardRepository.findByPrevAll(num, kwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public Board findByNext(String schType, String kwd, long num) {
		Board dto = null;
		
		try {
			if(kwd.length() == 0) {
				dto = boardRepository.findByNext(num);
			} else if(schType.equals("userName")) {
				dto = boardRepository.findByNextName(num, kwd);
			} else if(schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				dto = boardRepository.findByNextDate(num, kwd);
			} else if(schType.equals("subject")) {
				dto = boardRepository.findByNextSubject(num, kwd);
			} else if(schType.equals("content")) {
				dto = boardRepository.findByNextContent(num, kwd);
			} else if(schType.equals("all")) {
				dto = boardRepository.findByNextAll(num, kwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public void updateBoard(Board entity, String pathname) throws Exception {
		try {
			String saveFilename = fileManager.doFileUpload(entity.getSelectFile(), pathname);
			if (saveFilename != null) {
				if (entity.getSaveFilename() != null && entity.getSaveFilename().length() != 0) {
					fileManager.doFileDelete(entity.getSaveFilename(), pathname);
				}

				entity.setSaveFilename(saveFilename);
				entity.setOriginalFilename(entity.getSelectFile().getOriginalFilename());
			}
			
			// save : 존재하면 수정, 없으면 추가
			boardRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteBoard(long num, String pathname, long memberIdx, int userRole) throws Exception {
		try {
			Board entity = findById(num);
			if (entity == null) {
				return;
			}
			
			if(entity.getId() != memberIdx || userRole < 51) {
				return;
			}
			
			fileManager.doFileDelete(entity.getSaveFilename(), pathname);			
			
			boardRepository.deleteById(num);
			
/*
			Board dto = boardRepository.findById(num)
                    .orElseThrow(() -> new IllegalAccessError("[num=" + num + "] 해당 게시글이 존재하지 않습니다."));
            boardRepository.delete(board);      
*/                    			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteBoardFile(Board entity, String pathname) throws Exception {
		try {
			if (entity.getSaveFilename() != null) {
				fileManager.doFileDelete(entity.getSaveFilename(), pathname); // 실제파일삭제
				entity.setSaveFilename("");
				entity.setOriginalFilename("");
				
				updateBoard(entity, pathname); // DB 테이블의 파일명 변경(삭제)
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
