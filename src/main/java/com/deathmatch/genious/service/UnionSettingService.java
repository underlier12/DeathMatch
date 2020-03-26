package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.dao.UnionSettingDAO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionCardDTO.BackType;
import com.deathmatch.genious.domain.UnionCardDTO.ColorType;
import com.deathmatch.genious.domain.UnionCardDTO.ShapeType;
import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.UnionPlayerDTO;
import com.deathmatch.genious.domain.UnionSettingDTO;
import com.deathmatch.genious.util.UnionCombination;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionSettingService {
	
	private final GameRoomService gameRoomService;
	private final UnionCombination unionCombination;
	private final UnionSettingDAO unionSettingDAO;
	private final ObjectMapper objectMapper;

	private Map<String, Object> jsonMap;
	private UnionSettingDTO unionSettingDTO;
	private JSONObject jsonObject;
	private String jsonString;
	
	public void preprocessing() {
		jsonMap = new HashMap<>();
	}
	
	public void postprocessing() {
		jsonObject = new JSONObject(jsonMap);
		jsonString = jsonObject.toJSONString();
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
	}
	
	public void welcome(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		log.info("welcome");
		
		gameRoom.addSession(session);
		String status = decideStatus(gameRoom);
		
		UnionPlayerDTO unionPlayerDTO = new UnionPlayerDTO();
		
		unionPlayerDTO.setUserEmail(gameDTO.getSender());
		unionPlayerDTO.setRoomId(gameRoom.getRoomId());
		unionPlayerDTO.setStatus(status);
		unionPlayerDTO.setReady(false);
		unionPlayerDTO.setTurn(false);
		unionPlayerDTO.setScore(0);
		
		Map<String, Object> map = session.getAttributes();
		
		map.put("player", unionPlayerDTO);
		
		isEngaged(unionPlayerDTO, gameRoom);
	}
	
	public String decideStatus(GameRoom gameRoom) {
		String status = null;
		if(gameRoom.getSessions().size() == 1) status = "HOST";
		else if(gameRoom.getSessions().size() == 2) status = "OPPONENT";
		else status = "GUEST";
		
		return status;
	}
	
	public void isEngaged(UnionPlayerDTO player, GameRoom gameRoom) {
		switch (player.getStatus()) {
		case "HOST":
		case "OPPONENT":
			gameRoom.addPlayer(player);
			break;
		}
	}
	
	public UnionGameDTO join(UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
		gameDTO.setSender("Setting");
		
		return gameDTO;
	}
	
	public UnionSettingDTO ready(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		preprocessing();

		Map<String, Object> map = session.getAttributes();
		UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
		unionPlayerDTO.setReady(true);
		
		jsonMap.put("type", "READY");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		jsonMap.put("message", gameDTO.getSender() + "님이 준비하셨습니다.");
		
		postprocessing();

		return unionSettingDTO;
	}
	
	public boolean readyCheck(GameRoom gameRoom) {
		boolean isReady = false;
		int countReady = 0;
		
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		
		for(WebSocketSession sess : sessions) {
			Map<String, Object> map = sess.getAttributes();
			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
			switch (unionPlayerDTO.getStatus()) {
			case "HOST":
			case "OPPONENT":
				if(unionPlayerDTO.getReady().equals(true)) countReady++;
				break;
			default:
				break;
			}
		}
		if(countReady > 1) isReady = true;
		
    	return isReady;
	}
	
	public void startGame(GameRoom gameRoom) {
		gameRoom.setGameId(makeGameId());
	}
	

	public String makeGameId() {
		return UUID.randomUUID().toString();
	}
	
	public UnionSettingDTO standby(GameRoom gameRoom) {
		
		preprocessing();
		
		List<String> players = new ArrayList<>();
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		
		for(WebSocketSession sess : sessions) {
			Map<String, Object> map = sess.getAttributes();
			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
			
			if(unionPlayerDTO.getStatus().equals("HOST") ||
					unionPlayerDTO.getStatus().equals("OPPONENT")) {
				players.add(unionPlayerDTO.getUserEmail());
			}
		}
		
		jsonMap.put("type", "READY");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		jsonMap.put("message", "참가자들이 모두 준비를 마쳤습니다.\n곧 게임을 시작합니다.");
		jsonMap.put("user1", players.get(0));
		jsonMap.put("user2", players.get(1));
		
		postprocessing();
		
		return unionSettingDTO;
	}
	
	public UnionSettingDTO setUnionProblem(GameRoom gameRoom) {
		
		preprocessing();
		
		List<UnionCardDTO> problemList = unionSettingDAO.makeUnionProblem();
		List<String> problemCardNames = new ArrayList<>();
		
		log.info("problemList.toString : " + problemList.toString());
		
		for(int i = 0; i < problemList.size(); i++) {
			String cardName = problemList.get(i).getName();
			problemCardNames.add(cardName);
			unionSettingDAO.insertProblem(gameRoom, i, cardName);			
		}
		
		
		jsonMap.put("type", "PROBLEM");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		jsonMap.put("cards", problemCardNames);
		
		postprocessing();
		
		log.info("unionProblemDTO : " + unionSettingDTO + "\n");
		
		return unionSettingDTO;
	}
	
	public Set<String> makeUnionAnswer(List<UnionCardDTO> problemList,
			Set<UnionCardDTO[]>  answerCandidateSet) {
				
		Set<String> answerSet = new HashSet<>();
		for(UnionCardDTO[] answerCandidate : answerCandidateSet) {
			
			int satisfiedCondition = 0;

			Set<ShapeType> shapeList = new HashSet<>();
			Set<ColorType> colorList = new HashSet<>();
			Set<BackType> backList = new HashSet<>();
			
			for(int i = 0; i < 3; i++) {
				shapeList.add(answerCandidate[i].getShape());
				colorList.add(answerCandidate[i].getColor());
				backList.add(answerCandidate[i].getBackground());
			}
			
			if(shapeList.size() == 1 || shapeList.size() == 3) satisfiedCondition++;
			if(colorList.size() == 1 || colorList.size() == 3) satisfiedCondition++;
			if(backList.size() == 1 || backList.size() == 3) satisfiedCondition++;
			
			if(satisfiedCondition == 3) {
				
				int[] indices = new int[3];
				
				for(int i = 0; i < 3; i++) {
					indices[i] = problemList.indexOf(answerCandidate[i]) + 1;
				}
				
				Arrays.sort(indices);
				
				String answer = Arrays.toString(indices).replaceAll("[^0-9]","");
				answerSet.add(answer);
			}
		}
		return answerSet;
	}
	
	public void setUnionAnswer(GameRoom gameRoom){
		
		List<UnionCardDTO> problemList = unionSettingDAO.selectUnionProblem(gameRoom);
		
		Set<UnionCardDTO[]> answerCandidateSet = new HashSet<>();
		Set<String> answerSet = new HashSet<>();
		
		answerCandidateSet = unionCombination.makeCombination(problemList);
		answerSet = makeUnionAnswer(problemList, answerCandidateSet);
		
		unionSettingDAO.insertAnswer(gameRoom, answerSet);
	}	
	
	public void resetGame(GameRoom gameRoom) {
		gameRoom.setRound(0);
		
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		
		for(WebSocketSession sess : sessions) {
			Map<String, Object> map = sess.getAttributes();
			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
			
			unionPlayerDTO.setReady(false);
			unionPlayerDTO.setScore(0);
		}
	}
	
	public void bye(WebSocketSession session, CloseStatus status) {
		Map<String, Object> map = session.getAttributes();
		UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
				
		GameRoom gameRoom = gameRoomService.findRoomById(unionPlayerDTO.getRoomId());
		gameRoom.removeSession(session);
		
		log.info("bye");
		log.info(gameRoom.getSessions());
	}
}
