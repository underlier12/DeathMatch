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
	
//	public NoticeBoardController(NoticeBoardService noticeService) {
//		this.noticeService = noticeService;
//	}
	
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
	
//	@GetMapping("/answer-registration")
//	public String answerRegistGet(@ModelAttribute("cri") Criteria cri, @RequestParam("bno")
//			int bno,Model model) {
//		log.info("Write Answer Suggestion");
//		NoticeBoardDTO parentBoard = noticeService.read(bno);
//		log.info("Get Parent Board: " + parentBoard.toString());
//		model.addAttribute("Suggestion", parentBoard);
//		return "/suggestion/answer-registration";
//	}
	
	@PostMapping("/registration")
	public String registPost(@ModelAttribute NoticeBoardDTO noticeBoardDTO, RedirectAttributes rttr) {
		log.info("Register Suggestion " + noticeBoardDTO.toString());
		noticeService.insert(noticeBoardDTO);
		rttr.addFlashAttribute("msg", "공지글이 등록 되었습니다");
		return "redirect:/notice";
	}
	
//	@PostMapping("/answer-registration")
//	public String answerRegistPost(@ModelAttribute NoticeBoardDTO NoticeBoardDTO
//			, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr ) {
//		log.info("regist Answer Post " + NoticeBoardDTO.toString());
//		noticeService.registerAnswer(NoticeBoardDTO);
//		
//		rttr.addAttribute("page", cri.getPage());
//		rttr.addAttribute("perPageNum", cri.getPerPageNum());
//		
//		return "redirect:/suggestion";
//	}
	
//	@ResponseBody
//	@PostMapping("/registration/reply")
//	public ResponseEntity<String> registerReply(@RequestBody SuggestionReplyDTO suggestionReplyDTO){
//		log.info("insertReply ");
//		noticeService.insertReply(suggestionReplyDTO);
//		return new ResponseEntity<>("Success",HttpStatus.OK);
//	}
	
	@GetMapping("/content")
	public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) {
		log.info("게시글 번호 : " + bno);
		noticeService.increaseViews(bno);
		model.addAttribute("Suggestion",noticeService.read(bno));
	}
	
//	@ResponseBody
//	@GetMapping("/reply/{bno}" )
//	public ResponseEntity<List<SuggestionReplyDTO>> getReplyList(@PathVariable("bno") int bno){
//		log.info("getReplyList " + bno);
//		return new ResponseEntity<List<SuggestionReplyDTO>>(noticeService.getReplyList(bno),HttpStatus.OK);
//	}
	
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
	
//	@ResponseBody
//	@PostMapping("/deletion/reply/{rno}")
//	public ResponseEntity<String> deleteReply(@PathVariable("rno") int rno){
//		log.info("Delete Reply" + rno);
//		noticeService.deleteReply(rno);
//		return new ResponseEntity<>("Success",HttpStatus.OK);
//	}
		
}
