package com.deathmatch.genious.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/gameHome")
public class GameRoomController {

	private final GameRoomService gameRoomService;
	 
    @GetMapping
    public String createRoom(Model model) { 
    	log.info("=========== Enter ChatController =========");
    	log.info("=========== createRoom(String)-GET =========");
    	System.out.println();
    	model.addAttribute("rooms", gameRoomService.findAllRooms());
    	
    	return "gameHome";
    }
    
    @PostMapping
    public @ResponseBody ResponseEntity<String> createRoom(@RequestBody String name) {
    	
    	ResponseEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			log.info("=========== Enter ChatController =========");
			log.info("=========== createRoom(String)-POST =========");
			GameRoom newRoom = gameRoomService.createRoom(name);
			//headers.add("location", "/gameHome");
			log.info("new Room :" + newRoom);
			entity = new ResponseEntity<String>(headers,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return entity;
    	
    }
      
    @GetMapping("/{roomId}")
    public String room(@PathVariable String roomId, Model model, HttpSession httpSession) {
    	log.info("==================Enter ChatController =====================");
    	
    	GameRoom room = gameRoomService.findRoomById(roomId);
//    	String member = "member"; //session.getId();
    	
    	model.addAttribute("room", room);
    	model.addAttribute("member", httpSession.getId());
    	model.addAttribute("httpSession", httpSession);
    	
    	return "room";
    }
}
