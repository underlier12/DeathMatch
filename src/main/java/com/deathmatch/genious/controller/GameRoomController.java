package com.deathmatch.genious.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UserDTO;
import com.deathmatch.genious.service.GameRoomService;
import com.deathmatch.genious.util.Criteria;
import com.deathmatch.genious.util.PageMaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/gameHome")
public class GameRoomController {

	private final GameRoomService gameRoomService;
//	private final UnionSettingService unionSettingService;
	 
	//Criteria는 한 게시글 페이지 , PageMaker는 여러개의 게시글 페이지를 의미한다
    @GetMapping
    public void RoomList(Criteria cri,Model model) {
    	log.info(cri.toString());
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	pageMaker.setTotalCount(gameRoomService.countRoom());	
    	model.addAttribute("rooms",gameRoomService.findRoomList(cri));
    	model.addAttribute("pageMaker",pageMaker);
    }

	@ResponseBody
    @PostMapping
    public String createRoom(@RequestBody String name,Model model) {
		GameRoom newRoom = gameRoomService.createRoom(name);
		String currentRoomId = newRoom.getRoomId();
		log.info("makeRoom Id :" + currentRoomId);
		log.info("gameType: " + newRoom.getGameType() );
		return "/genious/gameHome/" +currentRoomId; 
    }
	
    @GetMapping("/{roomId}")
    public String room(@PathVariable String roomId, Model model, HttpSession httpSession) {    	
    	GameRoom room = gameRoomService.findRoomById(roomId);
    	UserDTO currentDTO = (UserDTO) httpSession.getAttribute("login");
    	
    	if(room == null) {
    		log.info("null exception");
    		model.addAttribute("msg", "해당 방은 사라졌습니다.");
    		return "gameHome";
    	}
    	
    	model.addAttribute("room", room);
    	model.addAttribute("member", currentDTO.getUserId());
//    	model.addAttribute("httpSession", httpSession);
    	return "room";
    }
      
    
}
