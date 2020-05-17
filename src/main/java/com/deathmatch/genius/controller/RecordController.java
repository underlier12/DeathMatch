package com.deathmatch.genius.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deathmatch.genius.domain.UserDTO;
import com.deathmatch.genius.service.RecordService;
import com.deathmatch.genius.util.Criteria;
import com.deathmatch.genius.util.PageDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/record")
public class RecordController {

	private final RecordService recordService;
	
	@GetMapping
	public String showHistory(Criteria criteria, Model model, HttpSession httpSession) {
		UserDTO currentDTO = (UserDTO) httpSession.getAttribute("login");
		String userId = currentDTO.getUserId();
		
		PageDTO pageDTO = new PageDTO(criteria, recordService.countRecord(userId));
		
		model.addAttribute("record", recordService.findRecordList(criteria, userId));
		model.addAttribute("pageMaker", pageDTO);
		
		return "record/record-history";
	}
}
