package com.deathmatch.genious.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
	private final List<UserDTO> userList = new ArrayList<>();

	/*
	 * @GetMapping public String allRooms(Model model, HttpSession session) { //
	 * Session을 통해서 User의 정보 값을 가져온다 UserDTO currentUser = (UserDTO)
	 * session.getAttribute("login"); userList.add(currentUser); for (int i = 1; i <
	 * userList.size(); i++) { log.info("userList " + i + " 번째 " + userList.get(i));
	 * } model.addAttribute("rooms", gameRoomService.findAllRooms());
	 * model.addAttribute("userList", userList); return "gameHome"; }
	 */

	@GetMapping
	public String allRooms(Model model, HttpSession session) {
		// Session을 통해서 User의 정보 값을 가져온다
		/*
		 * UserDTO currentUser = (UserDTO)session.getAttribute("login");
		 * userList.add(currentUser); for (int i = 1; i < userList.size(); i++) {
		 * log.info("userList " + i + " 번째 " + userList.get(i)); }
		 */
		model.addAttribute("rooms", gameRoomService.findAllRooms());
		String str[] = gameRoomService.userListArr();
		model.addAttribute("userList",str);
		return "gameHome";
	}

	/*
	 * @PostMapping public @ResponseBody ResponseEntity<String>
	 * createRoom(@RequestBody String name) { ResponseEntity<String> entity = null;
	 * HttpHeaders headers = new HttpHeaders(); try { GameRoom newRoom =
	 * gameRoomService.createRoom(name);
	 * 
	 * log.info("newRoom : " + newRoom); entity = new
	 * ResponseEntity<String>(headers, HttpStatus.OK); } catch (Exception e) {
	 * e.printStackTrace(); entity = new ResponseEntity<String>(e.getMessage(),
	 * HttpStatus.BAD_REQUEST); } return entity; }
	 */

	@PostMapping
	public @ResponseBody ResponseEntity<String> createRoom(@RequestBody String name, HttpSession session) {
		ResponseEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			GameRoom newRoom = gameRoomService.createRoom(name, session);

			log.info("newRoom : " + newRoom);
			entity = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

	@GetMapping("/{roomId}")
	public String room(@PathVariable String roomId, Model model, HttpSession httpSession) {
		GameRoom room = gameRoomService.findRoomById(roomId);

		model.addAttribute("room", room);
		model.addAttribute("member", httpSession.getId());
		model.addAttribute("httpSession", httpSession);

		return "room";
	}
}
