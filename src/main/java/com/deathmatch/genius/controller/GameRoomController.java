package com.deathmatch.genius.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.PageDTO;
import com.deathmatch.genius.domain.UserDTO;
import com.deathmatch.genius.service.GameRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/rooms")
public class GameRoomController {

	private final GameRoomService gameRoomService;

	//Criteria는 한 게시글 페이지 , PageMaker는 여러개의 게시글 페이지를 의미한다
    @GetMapping
    public String RoomList(Criteria cri,Model model) {
    	log.info(cri.toString());
    	PageDTO pageMaker = new PageDTO(cri,gameRoomService.countRoom());
    	//pageMaker.setCri(cri);
    	//pageMaker.setTotalCount(gameRoomService.countRoom());	
    	model.addAttribute("rooms",gameRoomService.findRoomList(cri));
    	model.addAttribute("pageMaker",pageMaker);
    	return "main/rooms";
    }

	@ResponseBody
    @PostMapping
    public String createRoom(@RequestBody Map<String, String> json, Model model) {
		String gameType = json.get("gameType");
		String currentRoomId;
		
		if(gameType.equals("union")) {
			GameRoom newRoom = (GameRoom) 
					gameRoomService.createRoom(json.get("gameType"), json.get("roomName"));
			currentRoomId = newRoom.getRoomId();			
		} else {
			IndianGameRoom newRoom = (IndianGameRoom) 
					gameRoomService.createRoom(json.get("gameType"), json.get("roomName"));
			currentRoomId = newRoom.getRoomId();			
		}
		log.info("makeRoom Id :" + currentRoomId);
//		log.info("gameType: " + newRoom.getGameType() );
		
		return "/rooms/" + gameType + "/" + currentRoomId; 
    }
	
    @GetMapping("/{gameType}/{roomId}")
    public String room(@PathVariable String gameType, @PathVariable String roomId
    								, Model model, HttpSession httpSession) {
    	
    	Object room = gameRoomService.findRoomById(roomId);
    	
    	if(room == null) {
    		log.info("null exception");
    		model.addAttribute("msg", "해당 방은 사라졌습니다.");
    		return "main/rooms";
    	}
    	
    	UserDTO currentDTO = (UserDTO) httpSession.getAttribute("login");
    	
    	model.addAttribute("room", room);
    	model.addAttribute("member", currentDTO.getUserId());
    	
    	return "game/" + gameType + "/" + gameType;
    }
      
    
}
