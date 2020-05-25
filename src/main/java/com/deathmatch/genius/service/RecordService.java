package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genius.dao.HistoryDTO;
import com.deathmatch.genius.dao.RecordDAO;
import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.IndianPlayerDTO;
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
	
	public Map<String, Object> indianPreprocessing(IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("gameId", indianRoom.getGameId());
		jsonMap.put("gameType", indianRoom.getGameType());
		
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
			jsonMap.put("winLose", player.getWinLose().toString());
			jsonMap.put("score", player.getScore());
			
			RecordDTO recordDTO = postprocessing(jsonMap);
			recordDAO.insertHistory(recordDTO);
		}
	}
	
	public void IndianRecordHistory(IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		
		for(IndianPlayerDTO player : players) {
			Map<String, Object> jsonMap = indianPreprocessing(indianRoom);
			
			jsonMap.put("userId", player.getUserId());
			jsonMap.put("winLose", player.getWinLose().toString());
			log.info("IndianRecoredHistory ");
			
			RecordDTO recordDTO = postprocessing(jsonMap);
			recordDAO.insertHistory(recordDTO);
		}
	}

	public int countRecord(String userId) {
		return recordDAO.countRecord(userId);
	}

	public List<HistoryDTO> findRecordList(Criteria criteria, String userId) {
		List<HistoryDTO> historyList = new ArrayList<>();
		
		criteria.setKeyword(userId);
		List<RecordDTO> recordList = recordDAO.selectAllRecord(criteria);
		
		for(RecordDTO record : recordList) {
			RecordDTO oppoRecord = recordDAO.selectOpponentRecord(record);
			HistoryDTO history = new HistoryDTO();
			
			history.embedUserRecord(record);
			history.embedOpponentRecord(oppoRecord);
			
			log.info(history);
			
			historyList.add(history);
		}
		
		return historyList;
	}

}
