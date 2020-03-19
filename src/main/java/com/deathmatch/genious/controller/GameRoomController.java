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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/gameHome")
public class GameRoomController {

	private final GameRoomService gameRoomService;
	 
	@GetMapping
    public String allRooms(Model model) {
    	model.addAttribute("rooms", gameRoomService.findAllRooms());
    	return "gameHome";
    } 

	@ResponseBody
    @PostMapping
    public String createRoom(@RequestBody String name,Model model) {
		GameRoom newRoom = gameRoomService.createRoom(name);
		String currentRoomId = newRoom.getRoomId();
		log.info("makeRoom Id :" + currentRoomId);
		return "/genious/gameHome/" +currentRoomId; 
    }
	
    @GetMapping("/{roomId}")
    public String room(@PathVariable String roomId, Model model, HttpSession httpSession) {    	
    	GameRoom room = gameRoomService.findRoomById(roomId);
    	UserDTO currentDTO = (UserDTO) httpSession.getAttribute("login");
    	model.addAttribute("room", room);
    	model.addAttribute("member", currentDTO.getUserEmail());
    	model.addAttribute("httpSession", httpSession);
    	return "room";
    }
    
    
}
