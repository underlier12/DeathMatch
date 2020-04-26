package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genius.dao.UnionLoadingDAO;
import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.UnionDatabaseDTO;
import com.deathmatch.genius.domain.UnionLoadingDTO;
import com.deathmatch.genius.domain.UnionPlayerDTO;
import com.deathmatch.genius.domain.UnionSettingDTO.MessageType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionLoadingService {

	public final UnionLoadingDAO unionLoadingDAO;
	public final ObjectMapper objectMapper;
		
	public Map<String, Object> preprocessing(MessageType type, String roomId) {
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("type", type.toString());
		jsonMap.put("roomId", roomId);
		jsonMap.put("sender", "Loader");
		
		return jsonMap;
	}
	
	public UnionLoadingDTO postprocessing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		UnionLoadingDTO unionLoadingDTO = null;		
		log.info("jsonString : " + jsonString);

		try {
			unionLoadingDTO = objectMapper.readValue(jsonString, UnionLoadingDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return unionLoadingDTO;
	}
	
	public UnionDatabaseDTO dbprocessing(GameRoom gameRoom){
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("round", gameRoom.getRound());
		
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		UnionDatabaseDTO unionDatabaseDTO = null;		
		log.info("jsonString : " + jsonString);
		
		try {
			unionDatabaseDTO = objectMapper.readValue(jsonString, UnionDatabaseDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return unionDatabaseDTO;
	}
	
	public Queue<Object> loadOnGame(GameRoom gameRoom){
		Queue<Object> queue = null;
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();

		if(!gameRoom.getPlaying()) {
			queue = beforeStart(engaged, gameRoom);			
		} else {
			queue = afterStart(engaged, gameRoom);
		}
		return queue;
	}
	
	private Queue<Object> beforeStart(List<UnionPlayerDTO> engaged, GameRoom gameRoom) {
		Queue<Object> queue = new LinkedList<>();

		switch (gameRoom.getEngaged().size()) {
		case 2:
			queue.offer(loadPlayer(engaged.get(1), gameRoom));
		case 1:
			queue.offer(loadPlayer(engaged.get(0), gameRoom));
		}
		return queue;
	}

	private Queue<Object> afterStart(List<UnionPlayerDTO> engaged, GameRoom gameRoom) {
		Queue<Object> queue = new LinkedList<>();
		queue = beforeStart(engaged, gameRoom);
		
		for(UnionPlayerDTO player : engaged) {
			queue.offer(loadScores(player, gameRoom));			
		}
		queue.offer(loadProblem(gameRoom));
		queue.offer(loadAnswersheet(gameRoom));
		
		return queue;
	}
	
	public UnionLoadingDTO loadPlayer(UnionPlayerDTO player, GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.LOAD, gameRoom.getRoomId());
		
		jsonMap.put("message", "PLAYER");
		jsonMap.put("user", player.getUserId());
		
		UnionLoadingDTO unionLoadingDTO = postprocessing(jsonMap);
		return unionLoadingDTO;
	}
	
	private UnionLoadingDTO loadScores(UnionPlayerDTO player, GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.LOAD, gameRoom.getRoomId());
		
		jsonMap.put("message", "SCORE");
		jsonMap.put("user", player.getUserId());
		jsonMap.put("score", player.getScore());
		
		UnionLoadingDTO unionLoadingDTO = postprocessing(jsonMap);
		return unionLoadingDTO;
	}

	private UnionLoadingDTO loadProblem(GameRoom gameRoom) {
		Map<String, Object> jsonMap;
		UnionDatabaseDTO dbDTO = dbprocessing(gameRoom);		
		List<String> problem = unionLoadingDAO.selectUnionProblemCardNames(dbDTO);
		
		jsonMap = preprocessing(MessageType.LOAD, gameRoom.getRoomId());
		
		jsonMap.put("message", "PROBLEM");
		jsonMap.put("set", problem);
		
		UnionLoadingDTO unionLoadingDTO = postprocessing(jsonMap);
		
		return unionLoadingDTO;
	}

	private UnionLoadingDTO loadAnswersheet(GameRoom gameRoom) {
		// TODO Auto-generated method stub
		return null;
	}

	public Queue<Object> loadOnHistory(){
		Queue<Object> queue = new LinkedList<>();

		return queue;
	}
}
