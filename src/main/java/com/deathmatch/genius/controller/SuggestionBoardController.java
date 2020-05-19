package com.deathmatch.genius.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;
import com.deathmatch.genius.service.SuggestionBoardService;
import com.deathmatch.genius.util.Criteria;
import com.deathmatch.genius.util.PageDTO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/suggestion")
@Log4j
public class SuggestionBoardController {
	
	private final SuggestionBoardService sugService;
	
	public SuggestionBoardController(SuggestionBoardService sugService) {
		this.sugService = sugService;
	}
	
	@GetMapping
	public String listAll(Criteria cri,Model model) {
		
		log.info("GetListWith Paging" + cri.toString());
		
		model.addAttribute("list", sugService.getListWithPaging(cri));
		model.addAttribute("pageMaker", new PageDTO(cri,sugService.totalCount(cri)));
		
		return "/suggestion/suggestionboard";
	}
	

	@GetMapping("/registration")
	public String registGet() {
		log.info("Write Suggestion");
		return "/suggestion/registration";
	}
	
	@GetMapping("/answer-registration")
	public String answerRegistGet(@ModelAttribute("cri") Criteria cri, @RequestParam("bno")
			int bno,Model model) {
		log.info("Write Answer Suggestion");
		SuggestionBoardDTO parentBoard = sugService.read(bno);
		log.info("Get Parent Board: " + parentBoard.toString());
		model.addAttribute("Suggestion", parentBoard);
		return "/suggestion/answer-registration";
	}
	
	@PostMapping("/registration")
	public String registPost(@ModelAttribute SuggestionBoardDTO suggestionBoardDTO,
				RedirectAttributes rttr) {
		log.info("Register Suggestion " + suggestionBoardDTO.toString());
		sugService.insert(suggestionBoardDTO);
		rttr.addFlashAttribute("msg", "건의글이 등록 되었습니다");
		return "redirect:/suggestion";
	}
	
	@PostMapping("/answer-registration")
	public String answerRegistPost(@ModelAttribute SuggestionBoardDTO suggestionBoardDTO
			, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr ) {
		log.info("regist Answer Post " + suggestionBoardDTO.toString());
		sugService.registerAnswer(suggestionBoardDTO);
		
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		
		return "redirect:/suggestion";
	}
	
	@ResponseBody
	@PostMapping("/registration/reply")
	public ResponseEntity<String> registerReply(@RequestBody SuggestionReplyDTO suggestionReplyDTO){
		log.info("insertReply ");
		sugService.insertReply(suggestionReplyDTO);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
	
	@GetMapping("/content")
	public void read(@RequestParam("bno") int bno, @ModelAttribute("cri")
			Criteria cri,Model model) {
		log.info("게시글 번호 : " + bno);
		sugService.increaseViews(bno);
		model.addAttribute("Suggestion",sugService.read(bno));
	}
	
	@ResponseBody
	@GetMapping("/reply/{bno}" )
	public ResponseEntity<List<SuggestionReplyDTO>> getReplyList(@PathVariable("bno") int bno){
		log.info("getReplyList " + bno);
		return new ResponseEntity<List<SuggestionReplyDTO>>(sugService.getReplyList(bno),HttpStatus.OK);
	}
	
	@GetMapping("/post-edit")
	public void modifyGet(@RequestParam("bno") int bno, @ModelAttribute("cri")
				Criteria cri,Model model) {
		log.info("수정 게시글 번호 " + bno);
		model.addAttribute("Suggestion", sugService.read(bno));
	}
	
	@PostMapping("/post-edit")
	public String modifyPost(SuggestionBoardDTO suggestionBoardDTO,@ModelAttribute("cri")
			Criteria cri,RedirectAttributes rttr) {
		sugService.update(suggestionBoardDTO);
		rttr.addFlashAttribute("msg","글이 수정 되었습니다");
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("bno",suggestionBoardDTO.getBno());
		
		return "redirect:/suggestion/content";
	}
	
	@PostMapping("/deleteion")
	public String delete(@RequestParam("bno") int bno,Criteria cri,
				RedirectAttributes rttr) {
		sugService.delete(bno);
		rttr.addFlashAttribute("msg", "SUC");
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		
		return "redirect:/suggestion";
	}
	
	@ResponseBody
	@PostMapping("/deletion/reply/{rno}")
	public ResponseEntity<String> deleteReply(@PathVariable("rno") int rno){
		log.info("Delete Reply" + rno);
		sugService.deleteReply(rno);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
		
}
