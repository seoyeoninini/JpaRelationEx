package com.sp.app.controller;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sp.app.common.FileManager;
import com.sp.app.domain.Board;
import com.sp.app.dto.SessionInfo;
import com.sp.app.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private FileManager fileManager;
	
	@RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(@RequestParam(name = "page", defaultValue = "1") int current_page,
			@RequestParam(name = "schType", defaultValue = "all") String schType,
			@RequestParam(name = "kwd", defaultValue = "") String kwd,
			HttpServletRequest req,
			Model model) throws Exception {
		
		int size = 10;
		int total_page = 0;
		long dataCount = 0;
		List<Board> list = null;
		
		try {
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			Page<Board> pageBoard = boardService.listPage(schType, kwd, current_page, size);
			total_page = pageBoard.getTotalPages();
			if(current_page > total_page) {
				current_page = total_page;
				pageBoard = boardService.listPage(schType, kwd, current_page, size);
			}
			
			dataCount = pageBoard.getTotalElements();
			
			list = pageBoard.getContent();
		} catch (Exception e) {
		}
		
		model.addAttribute("list", list);
		model.addAttribute("page", current_page);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("size", size);
		model.addAttribute("total_page", total_page);
		
		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);
		
		return "board/list";
	}
	
	@GetMapping("write")
	public String writeForm(HttpSession session, Model model) throws Exception {
		model.addAttribute("mode", "write");
		return "board/write";
	}
	
	@PostMapping("write")
	public String writeSubmit(Board dto, HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "board";
		
		try {
			dto.setId(info.getMemberIdx());
			boardService.insertBoard(dto, pathname);
		} catch (Exception e) {
		}
		
		return "redirect:/board/list";
	}
	
	@GetMapping("article/{num}")
	public String article(@PathVariable(name = "num") long num,
			@RequestParam(name = "page") String page,
			@RequestParam(name = "schType", defaultValue = "all") String schType,
			@RequestParam(name = "kwd", defaultValue = "") String kwd,
			Model model) throws Exception {
		
		kwd = URLDecoder.decode(kwd, "utf-8");
		String query = "page="+page;
		if(kwd.length() != 0) {
			query += "&schType="+schType+
					"&kwd="+URLEncoder.encode(kwd, "utf-8");
		}
		
		boardService.updateHitCount(num);
		
		Board dto = boardService.findById(num);
		if(dto == null) {
			return "redirect:/board/list?"+query;
		}
		
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		
		Board prevDto = boardService.findByPrev(schType, kwd, num);
		Board nextDto = boardService.findByNext(schType, kwd, num);
		
		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("page", page);
		model.addAttribute("query", query);
		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);
		
		return "board/article";
	}
	
	@GetMapping("delete")
	public String delete(@RequestParam(name = "num") long num,
			@RequestParam(name = "page") String page,
			@RequestParam(name = "schType", defaultValue = "all") String schType,
			@RequestParam(name = "kwd", defaultValue = "") String kwd,
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		kwd = URLDecoder.decode(kwd, "utf-8");
		String query = "page=" + page;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "board";
		
		try {
			// 자료 삭제
			boardService.deleteBoard(num, pathname, info.getMemberIdx(), info.getUserRole());
		} catch (Exception e) {
		}

		return "redirect:/board/list?" + query;
	}

	@GetMapping("update")
	public String updateForm(@RequestParam(name = "num") long num,
			@RequestParam(name = "page") String page,
			HttpSession session,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		Board dto = boardService.findById(num);
		if ( dto == null || dto.getId() != info.getMemberIdx() ) {
			return "redirect:/board/list?page=" + page;
		}

		model.addAttribute("mode", "update");
		model.addAttribute("page", page);
		model.addAttribute("dto", dto);

		return "board/write";
	}

	@PostMapping("update")
	public String updateSubmit(Board dto,
			@RequestParam(name = "page") String page,
			HttpSession session) throws Exception {

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "board";
		
		try {
			// 수정 하기
			boardService.updateBoard(dto, pathname);
		} catch (Exception e) {
		}

		return "redirect:/board/list?page=" + page;
	}
	
	@GetMapping("deleteFile")
	public String deleteFile(@RequestParam(name = "num") long num,
			@RequestParam(name = "page") String page,
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "board";

		Board dto = boardService.findById(num);
		if ( dto == null || dto.getId() != info.getMemberIdx() ) {
			return "redirect:/board/list?page=" + page;
		}

		try {
			boardService.deleteBoardFile(dto, pathname);
		} catch (Exception e) {
		}

		return "redirect:/board/update?num=" + num + "&page=" + page;
	}
	
	@GetMapping(value = "download")
	public void download(@RequestParam(name = "num") long num, 
			HttpServletResponse resp,
			HttpSession session) throws Exception {

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "board";

		Board dto = boardService.findById(num);

		if (dto != null) {
			boolean b = fileManager.doFileDownload(dto.getSaveFilename(), 
					dto.getOriginalFilename(), pathname, resp);
			if (b) {
				return;
			}
		}

		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print("<script>alert('파일 다운로드가 실패 했습니다.');history.back();</script>");
	}

}
