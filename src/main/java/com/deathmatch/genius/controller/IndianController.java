/*
 * package com.deathmatch.genius.controller;
 * 
 * import javax.servlet.http.HttpSession; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * com.deathmatch.genius.domain.IndianGameRoom; import
 * com.deathmatch.genius.domain.UserDTO; import
 * com.deathmatch.genius.service.IndianGameRoomService; import
 * lombok.extern.log4j.Log4j;
 * 
 * @Log4j
 * 
 * @Controller
 * 
 * @RequestMapping("/indianHome") public class IndianController {
 * 
 * private final IndianGameRoomService indianRoomService;
 * 
 * public IndianController(IndianGameRoomService indianRoomService) {
 * this.indianRoomService = indianRoomService; }
 * 
 * @GetMapping public String roomList(Model model) {
 * model.addAttribute("indianRooms",indianRoomService.findAllRoom()); return
 * "main/indianHome"; }
 * 
 * @ResponseBody
 * 
 * @PostMapping public String creatRoom(@RequestBody String roomName) {
 * IndianGameRoom indianRoom = indianRoomService.createRoom(roomName); String
 * currentRoomId = indianRoom.getRoomId(); log.info("currnetRoomId: " +
 * currentRoomId); return "/indianHome/" + currentRoomId; }
 * 
 * 
 * @GetMapping("/{roomId}") public String room(@PathVariable String roomId,
 * Model model, HttpSession httpSession) { IndianGameRoom room =
 * indianRoomService.findRoomById(roomId); UserDTO currentDTO
 * =(UserDTO)httpSession.getAttribute("login"); log.info(roomId);
 * model.addAttribute("room", room);
 * model.addAttribute("member",currentDTO.getUserId()); return
 * "game/indian/indian"; }
 * 
 * 
 * }
 */