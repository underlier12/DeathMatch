package com.deathmatch.genius.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		public String showHistory(Criteria criteria, Model model) {
			
			PageDTO pageDTO = new PageDTO(criteria, recordService.countRecord());
			
			model.addAttribute("record", recordService.findRecordList(criteria));
			model.addAttribute("pageMaker", pageDTO);
			
			return "record/record-history";
		}
}
