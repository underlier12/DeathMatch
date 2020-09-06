package com.deathmatch.genius.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j
@Controller
public class HomeController {

	@GetMapping
	public String home() {
		return "index";
	}

	@GetMapping("/accessError")
	public String accessDenied(Authentication authentication, Model model){
		log.info("요청이 거절 되었습니다 " + authentication);
		model.addAttribute("msg","요청이 거부 되었습니다");
		return "/accessError";
	}

}
