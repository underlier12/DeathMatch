package com.deathmatch.genius.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/suggestion")
@Log4j
public class SuggestionBoardController {

	@GetMapping("/registration")
	public String write() {
		log.info("Write Suggestion");
		return "/suggestion/registration";
	}
}
