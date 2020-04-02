package com.deathmatch.genius.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.deathmatch.genius.service.GameRoomService;

import lombok.extern.log4j.Log4j;

@Log4j
public class GameRoomServiceTest {
	
	@Test
	public void testDestroyRoom(String roomId) {
		GameRoomService gameRoomService = new GameRoomService();
//		gameRoomService.createRoom("test1");
		
		List<String> gameRoomList = gameRoomService.findAllId();
		log.info("gameRoomList : " + gameRoomList);
		
		String addedRoomId = gameRoomList.get(0);
		log.info("addedRoomId : " + addedRoomId);
		
//		assertEquals(expecteds, actuals);
//		assertEquals(gameRoomService, gameRoomService.destroyRoom(addedRoomId));
	}
}
