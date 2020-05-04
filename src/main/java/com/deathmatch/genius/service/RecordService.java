package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genius.dao.RecordDAO;
import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.RecordDTO;
import com.deathmatch.genius.domain.UnionPlayerDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class RecordService {
	
	private final RecordDAO recordDAO;
	private final ObjectMapper objectMapper;
	
	public Map<String, Object> preprocessing(GameRoom gameRoom) {
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("gameType", gameRoom.getGameType());
		
		return jsonMap;
	}
	
	public RecordDTO postprocessing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		RecordDTO recordDTO = null;		
		log.info("jsonString : " + jsonString);

		try {
			recordDTO = objectMapper.readValue(jsonString, RecordDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recordDTO;
	}
	
	public void recordHistory(GameRoom gameRoom) {
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		
		for(UnionPlayerDTO player : engaged) {
			Map<String, Object> jsonMap = preprocessing(gameRoom);
			
			jsonMap.put("userId", player.getUserId());
			jsonMap.put("winLose", player.getWinLose());
			jsonMap.put("score", player.getScore());
			
			RecordDTO recordDTO = postprocessing(jsonMap);
			recordDAO.insertHistory(recordDTO);
		}
	}

}
