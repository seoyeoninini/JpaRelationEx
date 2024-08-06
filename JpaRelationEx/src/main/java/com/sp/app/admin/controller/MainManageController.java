package com.sp.app.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainManageController {
	@GetMapping("/admin")
	public String main() {
		return "admin/main";
	}
}
