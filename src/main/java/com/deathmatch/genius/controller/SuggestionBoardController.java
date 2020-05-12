package com.deathmatch.genius.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.service.SuggestionBoardService;
import com.deathmatch.genius.util.Criteria;
import com.deathmatch.genius.util.PageMaker;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/suggestion")
@Log4j
public class SuggestionBoardController {
	
	private final SuggestionBoardService sugService;
	
	public SuggestionBoardController(SuggestionBoardService sugService) {
		this.sugService = sugService;
	}

	@GetMapping("/registration")
	public String registGet() {
		log.info("Write Suggestion");
		return "/suggestion/registration";
	}
	
	@PostMapping("/registration")
	public String registPost(@ModelAttribute SuggestionBoardDTO suggestionBoardDTO,
				RedirectAttributes rttr) {
		log.info("regist Post " + suggestionBoardDTO.toString());
		sugService.insert(suggestionBoardDTO);
		rttr.addFlashAttribute("msg", "건의글이 등록 되었습니다");
		return "redirect:/suggestion/suggestionBoard";
	}
	
	@GetMapping("/content")
	public void read(@RequestParam("bno") int bno, @ModelAttribute("cri")
			Criteria cri,Model model) {
		log.info("게시글 번호 : " + bno);
		model.addAttribute("Suggestion",sugService.read(bno));
	}
	
	@GetMapping("/post-edit")
	public void modifyGet(int bno, Model model) {
		model.addAttribute("Suggestion", sugService.read(bno));
	}
	
	@PostMapping("/post-edit")
	public String modifyPost(SuggestionBoardDTO suggestionBoardDTO,@ModelAttribute("cri")
			Criteria cri,RedirectAttributes rttr) {
		sugService.update(suggestionBoardDTO);
		log.info("ModifyPost");
		log.info(suggestionBoardDTO.toString());
		rttr.addFlashAttribute("msg","글이 수정 되었습니다");
		return "redirect:/suggestion/suggestionBoard";
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam("bno") int bno,Criteria cri,
				RedirectAttributes rttr) {
		sugService.delete(bno);
		rttr.addFlashAttribute("msg", "SUC");
		rttr.addFlashAttribute("page", cri.getPage());
		rttr.addFlashAttribute("perPageNum", cri.getPerPageNum());
		
		return "redirect:/suggestion/suggestionBoard";
	}

	@GetMapping("/suggestionBoard")
	public String listAll(Criteria cri,Model model) {
		log.info("GetListWith Paging");
		model.addAttribute("list", sugService.getListWithPaging(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(sugService.totalCount(cri));
		model.addAttribute("pageMaker",pageMaker);
		return "/suggestion/suggestionBoard";
	}
	
}
