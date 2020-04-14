package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genius.dao.UnionSettingDAO;
import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.UnionCardDTO;
import com.deathmatch.genius.domain.UnionDatabaseDTO;
import com.deathmatch.genius.domain.UnionGameDTO;
import com.deathmatch.genius.domain.UnionPlayerDTO;
import com.deathmatch.genius.domain.UnionSettingDTO;
import com.deathmatch.genius.domain.UnionPlayerDTO.StatusType;
import com.deathmatch.genius.domain.UnionSettingDTO.MessageType;
import com.deathmatch.genius.util.UnionCombination;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionSettingService {
	
	private final UnionCombination unionCombination;
	private final UnionSettingDAO unionSettingDAO;
	private final ObjectMapper objectMapper;

	public Map<String, Object> preprocessing(MessageType type, String roomId) {
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("type", type.toString());
		jsonMap.put("roomId", roomId);
		jsonMap.put("sender", "Setting");
		
		return jsonMap;
	}
	
	public UnionSettingDTO postprocessing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		UnionSettingDTO unionSettingDTO = null;		
		log.info("jsonString : " + jsonString);

		try {
			unionSettingDTO = objectMapper.readValue(jsonString, UnionSettingDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return unionSettingDTO;
	}
	
	public Map<String, Object> dbPreprocessing(GameRoom gameRoom){
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("round", gameRoom.getRound());
		
		return jsonMap;
	}
	
	public UnionDatabaseDTO dbPostprocessing(Map<String, Object> jsonMap) {
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
	
//	public UnionSettingDTO loadPlayer(UnionPlayerDTO player, GameRoom gameRoom) {
//		Map<String, Object> jsonMap = preprocessing(MessageType.LOAD, gameRoom.getRoomId());
//		
//		jsonMap.put("message", "PLAYER");
//		jsonMap.put("user1", player.getUserId());
//		
//		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
//		return unionSettingDTO;
//	}
	
	public UnionSettingDTO join(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		Map<String, Object> jsonMap =  preprocessing(MessageType.JOIN, gameRoom.getRoomId());
		gameRoom.addSession(session);

		jsonMap.put("message", gameDTO.getSender() + "님이 입장했습니다.");
		jsonMap.put("user1", gameDTO.getSender());
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		return unionSettingDTO;
	}
	
	public void register(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		StatusType status = decideStatus(gameRoom);
		
		UnionPlayerDTO player = UnionPlayerDTO.builder()
				.userId(gameDTO.getSender())
				.roomId(gameRoom.getRoomId())
				.status(status)
				.ready(false)
				.turn(false)
				.score(0)
				.build();
		
		Map<String, Object> map = session.getAttributes();
		map.put("player", player);
		
		if(!isGuest(player)) gameRoom.addPlayer(player);
//		isEngaged(player, gameRoom);
	}
	
	public StatusType decideStatus(GameRoom gameRoom) {
		StatusType status;
		
		if(gameRoom.getEngaged().size() == 0) status = StatusType.HOST;
		else if(gameRoom.getEngaged().size() == 1) status = StatusType.OPPONENT;
		else status = StatusType.GUEST;
		
		return status;
	}
	
//	public void isEngaged(UnionPlayerDTO player, GameRoom gameRoom) {
//		if(player.getStatus() == StatusType.HOST || 
//				player.getStatus() == StatusType.OPPONENT) {
//			gameRoom.addPlayer(player);
//		}
//	}
	
	public UnionSettingDTO ready(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.READY, gameRoom.getRoomId());
		UnionPlayerDTO player = (UnionPlayerDTO) session.getAttributes().get("player");
		player.setReady(true);

		jsonMap.put("message", gameDTO.getSender() + "님이 준비하셨습니다.");
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		return unionSettingDTO;
	}
	
	public boolean readyCheck(GameRoom gameRoom) {
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
    	return engaged.get(0).getReady() && engaged.get(1).getReady();
	}
	
	public void startGame(GameRoom gameRoom) {
		gameRoom.setGameId(UUID.randomUUID().toString());
		gameRoom.setPlaying(true);
	}
	
	public UnionSettingDTO standby(GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.READY, gameRoom.getRoomId());

		jsonMap.put("message", "참가자들이 모두 준비를 마쳤습니다.\n게임을 시작합니다.");
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		return unionSettingDTO;
	}
	
	public UnionSettingDTO setUnionProblem(GameRoom gameRoom) {
		
		Map<String, Object> jsonMap = preprocessing(MessageType.PROBLEM, gameRoom.getRoomId());
		Map<String, Object> dbJsonMap = dbPreprocessing(gameRoom);
		
		List<UnionCardDTO> problemList = unionSettingDAO.makeUnionProblem();
		List<String> problemCardNames = problemList.stream()
													.map(c -> c.getName())
													.collect(Collectors.toList());
		
		UnionDatabaseDTO dbDTO = dbPostprocessing(dbJsonMap);
		unionSettingDAO.insertProblem(dbDTO, problemCardNames);
		
		jsonMap.put("cards", problemCardNames);
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		return unionSettingDTO;
	}
	
	public Set<String> makeUnionAnswer(List<UnionCardDTO> problemList,
			Set<UnionCardDTO[]>  answerCandidateSet) {
			
		Set<String> answerSet = new HashSet<>();
		
		for(UnionCardDTO[] answerCandidate : answerCandidateSet) {
			int shape = (int) Arrays.stream(answerCandidate)
					.map(UnionCardDTO::getShape).distinct().count();
						
			int color = (int) Arrays.stream(answerCandidate)
					.map(UnionCardDTO::getColor).distinct().count();
						
			int back = (int) Arrays.stream(answerCandidate)
					.map(UnionCardDTO::getBackground).distinct().count();
						
			if(shape != 2 && color != 2 && back != 2) {
				String answer = "";
				for(UnionCardDTO ans : answerCandidate) {
					answer += String.valueOf(problemList.indexOf(ans)+1);
				}
				answerSet.add(answer);
			}
		}
		return answerSet;
	}
	
	public void setUnionAnswer(GameRoom gameRoom){
		Map<String, Object> dbJsonMap = dbPreprocessing(gameRoom);
		UnionDatabaseDTO dbDTO = dbPostprocessing(dbJsonMap);
		
		List<UnionCardDTO> problemList = unionSettingDAO.selectUnionProblem(dbDTO);
		
		Set<UnionCardDTO[]> answerCandidateSet = unionCombination.makeCombination(problemList);
		Set<String> answerSet = makeUnionAnswer(problemList, answerCandidateSet);
		
		for(String ans : answerSet) log.info("answer : " + ans);
		
		if(answerSet.size() != 0) unionSettingDAO.insertAnswer(dbDTO, answerSet);
	}	
	
	public void resetGame(GameRoom gameRoom) {
		gameRoom.setRound(0);
		
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		for(UnionPlayerDTO player : engaged) {
			player.setReady(false);
			player.setScore(0);
		}
	}
	
	public void quitSession(WebSocketSession session, GameRoom gameRoom, CloseStatus status) {
		gameRoom.removeSession(session);
		log.info("bye");
	}
	
	public Boolean isPlaying(GameRoom gameRoom) {
		return gameRoom.getPlaying() && !gameRoom.getLastGameDTO().equals(null);
	}
	
	public Boolean isGuest(UnionPlayerDTO player) {
		return player.getStatus() == StatusType.GUEST;
	}
	
	public UnionSettingDTO quitPlayer(UnionPlayerDTO player, GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.LEAVE, gameRoom.getRoomId());
		gameRoom.removePlayer(player);		
		
		jsonMap.put("message", player.getUserId() + "님이 퇴장했습니다.");
		jsonMap.put("user1", player.getUserId());
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		return unionSettingDTO;
	}
}