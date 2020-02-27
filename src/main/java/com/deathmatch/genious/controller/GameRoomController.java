package com.deathmatch.genious.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.service.GameRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/game")
public class GameRoomController {

	private final GameRoomService gameRoomService;
	 
    @GetMapping
    public String createRoom() {
    	
    	System.out.println("=========== Enter ChatController =========");
    	System.out.println("=========== createRoom(String)-GET =========");
    	System.out.println();
    	
    	return "room-create";
    }
    
    @PostMapping
    public String createRoom(@RequestParam String name) {
    	
    	System.out.println("=========== Enter ChatController =========");
    	System.out.println("=========== createRoom(String)-POST =========");
    	
    	GameRoom newRoom = gameRoomService.createRoom(name);
    	
    	System.out.println("newRoom : " + newRoom);
    	
        return "redirect:/game/rooms";
    }
    
    @GetMapping("/rooms")
    public String findAllRooms(Model model) {
    	model.addAttribute("rooms", gameRoomService.findAllRooms());
    	
    	System.out.println("=========== Enter ChatController =========");
    	System.out.println("=========== findAllRooms(Model) =========");
    	System.out.println();
    	
        return "room-list";
    }
    
    @GetMapping("/rooms/{roomId}")
    public String room(@PathVariable String roomId, Model model) {
    	System.out.println("=========== Enter ChatController =========");
    	System.out.println();
    	
    	GameRoom room = gameRoomService.findRoomById(roomId);
    	String member = "member"; //session.getId();
    	
    	model.addAttribute("room", room);
    	model.addAttribute("member", member);
    	
    	return "room";
    }
}
