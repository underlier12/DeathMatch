package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genius.dao.UnionLoadingDAO;
import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.UnionLoadingDTO;
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
	
	public Queue<Object> loadOnGame(GameRoom gameRoom){
		Queue<Object> queue = new LinkedList<>();

		if(!gameRoom.getPlaying()) {
			beforeStart(gameRoom);			
		} else {
			afterStart(gameRoom);
		}
		return queue;
	}
	
	private void beforeStart(GameRoom gameRoom) {
		// TODO Auto-generated method stub
		
	}

	private void afterStart(GameRoom gameRoom) {
		// TODO Auto-generated method stub
		
	}

	public Queue<Object> loadOnHistory(){
		Queue<Object> queue = new LinkedList<>();

		return queue;
	}
}
