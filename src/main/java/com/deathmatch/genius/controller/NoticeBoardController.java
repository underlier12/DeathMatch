package com.deathmatch.genius.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.NoticeBoardDTO;
import com.deathmatch.genius.domain.PageDTO;
import com.deathmatch.genius.service.NoticeBoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeBoardController {
	
	private final NoticeBoardService noticeService;
	
	@GetMapping
	public String listAll(Criteria cri,Model model) {
		
		log.info("GetListWith Paging" + cri.toString());
		
		model.addAttribute("list", noticeService.getListWithPaging(cri));
		model.addAttribute("pageMaker", new PageDTO(cri,noticeService.totalCount(cri)));
		
		return "/notice/notice-board";
	}
	

	@GetMapping("/registration")
	public String registGet() {
		log.info("Write notice");
		return "/notice/registration";
	}
	
	@PostMapping("/registration")
	public String registPost(@ModelAttribute NoticeBoardDTO noticeBoardDTO, RedirectAttributes rttr) {
		log.info("Register Suggestion " + noticeBoardDTO.toString());
		noticeService.insert(noticeBoardDTO);
		rttr.addFlashAttribute("msg", "공지글이 등록 되었습니다");
		return "redirect:/notice";
	}
	
	@GetMapping("/content")
	public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) {
		log.info("게시글 번호 : " + bno);
		noticeService.increaseViews(bno);
		model.addAttribute("notice",noticeService.read(bno));
	}

	@GetMapping("/post-edit")
	public void modifyGet(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) {
		log.info("수정 게시글 번호 " + bno);
		model.addAttribute("notice", noticeService.read(bno));
	}
	
	@PostMapping("/post-edit")
	public String modifyPost(NoticeBoardDTO noticeBoardDTO,
			@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		noticeService.update(noticeBoardDTO);
		rttr.addFlashAttribute("msg","글이 수정 되었습니다");
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("bno",noticeBoardDTO.getBno());
		
		return "redirect:/notice/content";
	}
	
	@PostMapping("/deleteion")
	public String delete(@RequestParam("bno") int bno,Criteria cri, RedirectAttributes rttr) {
		noticeService.delete(bno);
		rttr.addFlashAttribute("msg", "SUC");
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		
		return "redirect:/notice";
	}
		
}
