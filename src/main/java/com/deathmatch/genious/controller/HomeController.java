package com.deathmatch.genious.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.deathmatch.genious.domain.UserDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class HomeController {

	@GetMapping
	public String home() {
		return "index";
	}
	
	
	// 세션 테스트용 -> 후에 삭제할 예정
	@GetMapping("/testView")
	public String testView(HttpSession session) {
		UserDTO user = (UserDTO)session.getAttribute("login");
		log.info("session User :" + user.getUserEmail());
		
		return "testView";
	}
	
	

}
