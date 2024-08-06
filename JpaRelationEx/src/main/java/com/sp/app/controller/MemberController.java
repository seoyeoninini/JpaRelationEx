package com.sp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sp.app.dto.SessionInfo;
import com.sp.app.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/member/*")
public class MemberController {
	private final MemberService memberService;
	
	@GetMapping("login")
	public String loginForm() {
		return "member/login";
	}
	
	@PostMapping("login")
	public String loginSubmit(@RequestParam(name = "userId") String userId,
			@RequestParam(name = "userPwd") String userPwd,
			HttpSession session,
			Model model) {
		
		SessionInfo info = memberService.signIn(userId, userPwd);
		if(info == null) {
			model.addAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
			return "member/login";
		}

		// 세션에 로그인 정보 저장
		session.setMaxInactiveInterval(30 * 60); // 세션유지시간 30분, 기본:30분
		session.setAttribute("member", info);
		
		// 로그인 이전 URI로 이동
		String uri = (String) session.getAttribute("preLoginURI");
		session.removeAttribute("preLoginURI");
		if (uri == null) {
			uri = "redirect:/";
		} else {
			uri = "redirect:" + uri;
		}

		return uri;	
	}
	
	@GetMapping(value = "logout")
	public String logout(HttpSession session) {
		// 세션에 저장된 정보 지우기
		session.removeAttribute("member");

		// 세션에 저장된 모든 정보 지우고, 세션초기화
		session.invalidate();

		return "redirect:/";
	}
	
	@GetMapping("noAuthorized")
	public String noAuthorized(HttpSession session) {
		return "member/noAuthorized";
	}	
}
