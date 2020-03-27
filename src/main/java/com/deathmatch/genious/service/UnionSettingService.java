package com.deathmatch.genious.service;

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

import com.deathmatch.genious.dao.UnionSettingDAO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionCardDTO.BackType;
import com.deathmatch.genious.domain.UnionCardDTO.ColorType;
import com.deathmatch.genious.domain.UnionCardDTO.ShapeType;
import com.deathmatch.genious.domain.UnionDatabaseDTO;
import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.UnionPlayerDTO;
import com.deathmatch.genious.domain.UnionPlayerDTO.StatusType;
import com.deathmatch.genious.domain.UnionSettingDTO;
import com.deathmatch.genious.domain.UnionSettingDTO.MessageType;
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
	
//	private final GameRoomService gameRoomService;
	private final UnionCombination unionCombination;
	private final UnionSettingDAO unionSettingDAO;
	private final ObjectMapper objectMapper;

//	private Map<String, Object> jsonMap;
//	private UnionSettingDTO unionSettingDTO;
//	private JSONObject jsonObject;
//	private String jsonString;
	
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
		
		log.info("jsonString : " + jsonString);

		UnionSettingDTO unionSettingDTO = null;
		try {
			unionSettingDTO = 
					objectMapper.readValue(jsonString, UnionSettingDTO.class);
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
		
		log.info("jsonString : " + jsonString);

		UnionDatabaseDTO unionDatabaseDTO = null;
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
	
//	public void welcome(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
//		log.info("welcome");
//		
//		gameRoom.addSession(session);
//		
//		if(isRejoin(gameDTO, gameRoom)) {
//			resumeGame();
//		} else {
//			register(session, gameDTO, gameRoom);
//		}
//		
//	}

	public UnionSettingDTO loadPlayer(UnionPlayerDTO player, GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.LOAD, gameRoom.getRoomId());
		
//		jsonMap.put("type", "LOAD");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
		
		jsonMap.put("message", "PLAYER");
		jsonMap.put("user1", player.getUserId());
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		
		return unionSettingDTO;
	}
	
	public UnionSettingDTO join(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		log.info("join");
		gameRoom.addSession(session);

		Map<String, Object> jsonMap =  preprocessing(MessageType.JOIN, gameRoom.getRoomId());
		
//		jsonMap.put("type", "JOIN");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
		
		jsonMap.put("message", gameDTO.getSender() + "님이 입장했습니다.");
		jsonMap.put("user1", gameDTO.getSender());
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		
		return unionSettingDTO;
	}
	
//	public Boolean isRejoin(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
//		boolean isRejoin = false;
//		
//		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
//		for(UnionPlayerDTO player : engaged) {
//			log.info("player : " + player.getUserEmail() + 
//					" sender : " + gameDTO.getSender());
//			if(player.getUserEmail().equals(gameDTO.getSender())) {
//				Map<String, Object> map = session.getAttributes();
//				map.put("player", player);
//				isRejoin = true;
//				break;
//			}
//		}
//		
//		return isRejoin;
//	}
	
//	public UnionSettingDTO resumeGame(GameRoom gameRoom) {
//		preprocessing();
//		
//		jsonMap.put("type", "RESUME");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
//		jsonMap.put("message", "게임을 재개합니다.");
//		
//		postprocessing();
//
//		return unionSettingDTO;
//	}
	
	public void register(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		StatusType status = decideStatus(gameRoom);
		
//		UnionPlayerDTO unionPlayerDTO = new UnionPlayerDTO();
//		
//		unionPlayerDTO.setUserEmail(gameDTO.getSender());
//		unionPlayerDTO.setRoomId(gameRoom.getRoomId());
//		unionPlayerDTO.setStatus(status);
//		unionPlayerDTO.setReady(false);
//		unionPlayerDTO.setTurn(false);
//		unionPlayerDTO.setScore(0);
		
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
		
		isEngaged(player, gameRoom);
		
	}
	
	public StatusType decideStatus(GameRoom gameRoom) {
		StatusType status;
		if(gameRoom.getEngaged().size() == 0) status = StatusType.HOST;
		else if(gameRoom.getEngaged().size() == 1) status = StatusType.OPPONENT;
		else status = StatusType.GUEST;
		
		return status;
	}
	
	public void isEngaged(UnionPlayerDTO player, GameRoom gameRoom) {
		if(player.getStatus() == StatusType.HOST || 
				player.getStatus() == StatusType.OPPONENT) {
			gameRoom.addPlayer(player);
		}
		
//		switch (player.getStatus()) {
//		case "HOST":
//		case "OPPONENT":
//			gameRoom.addPlayer(player);
//			break;
//		}
	}
	
	
	public UnionSettingDTO ready(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		UnionPlayerDTO player = (UnionPlayerDTO) session.getAttributes().get("player");
		player.setReady(true);

		Map<String, Object> jsonMap = preprocessing(MessageType.READY, gameRoom.getRoomId());
		
//		jsonMap.put("type", "READY");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
		
		jsonMap.put("message", gameDTO.getSender() + "님이 준비하셨습니다.");
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);

		return unionSettingDTO;
	}
	
	public boolean readyCheck(GameRoom gameRoom) {
		boolean isReady = false;
//		int countReady = 0;
		
//		Set<WebSocketSession> sessions = gameRoom.getSessions();
//		
//		for(WebSocketSession sess : sessions) {
//			Map<String, Object> map = sess.getAttributes();
//			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
//			switch (unionPlayerDTO.getStatus()) {
//			case "HOST":
//			case "OPPONENT":
//				if(unionPlayerDTO.getReady().equals(true)) countReady++;
//				break;
//			default:
//				break;
//			}
//		}
//		if(countReady > 1) isReady = true;
		
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		if(engaged.get(0).getReady() && engaged.get(1).getReady()) isReady = true;
		
    	return isReady;
	}
	
	public void startGame(GameRoom gameRoom) {
		gameRoom.setGameId(UUID.randomUUID().toString());
		gameRoom.setPlaying(true);
	}
	

//	public String makeGameId() {
//		return UUID.randomUUID().toString();
//	}
	
	public UnionSettingDTO standby(GameRoom gameRoom) {
		
		Map<String, Object> jsonMap = preprocessing(MessageType.READY, gameRoom.getRoomId());
//		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		
//		List<String> players = new ArrayList<>();
//		Set<WebSocketSession> sessions = gameRoom.getSessions();
		
//		for(WebSocketSession sess : sessions) {
//			Map<String, Object> map = sess.getAttributes();
//			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
//			
//			if(unionPlayerDTO.getStatus().equals("HOST") ||
//					unionPlayerDTO.getStatus().equals("OPPONENT")) {
//				players.add(unionPlayerDTO.getUserEmail());
//			}
//		}
		
//		jsonMap.put("type", "READY");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
		
		
		jsonMap.put("message", "참가자들이 모두 준비를 마쳤습니다.\n곧 게임을 시작합니다.");
//		jsonMap.put("user1", engaged.get(0));
//		jsonMap.put("user2", engaged.get(1));
		
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
		
//		log.info("problemList.toString : " + problemList.toString());
		
//		for(int i = 0; i < problemList.size(); i++) {
//			String cardName = problemList.get(i).getName();
//			problemCardNames.add(cardName);
//			unionSettingDAO.insertProblem(gameRoom, i, cardName);			
//		}
		
		
//		jsonMap.put("type", "PROBLEM");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
		
		UnionDatabaseDTO dbDTO = dbPostprocessing(dbJsonMap);
		unionSettingDAO.insertProblem(dbDTO, problemCardNames);
		
		jsonMap.put("cards", problemCardNames);
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
		
//		log.info("unionProblemDTO : " + unionSettingDTO + "\n");
		
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
		
		Map<String, Object> dbJsonMap = dbPreprocessing(gameRoom);
		UnionDatabaseDTO dbDTO = dbPostprocessing(dbJsonMap);
		
		List<UnionCardDTO> problemList = unionSettingDAO.selectUnionProblem(dbDTO);
		
		Set<UnionCardDTO[]> answerCandidateSet = new HashSet<>();
		Set<String> answerSet = new HashSet<>();
		
		answerCandidateSet = unionCombination.makeCombination(problemList);
		answerSet = makeUnionAnswer(problemList, answerCandidateSet);
		
		unionSettingDAO.insertAnswer(dbDTO, answerSet);
	}	
	
	public void resetGame(GameRoom gameRoom) {
		gameRoom.setRound(0);
		
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		for(UnionPlayerDTO player : engaged) {
			player.setReady(false);
			player.setScore(0);
		}
		
//		Set<WebSocketSession> sessions = gameRoom.getSessions();
//		
//		for(WebSocketSession sess : sessions) {
//			Map<String, Object> map = sess.getAttributes();
//			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
//			
//			unionPlayerDTO.setReady(false);
//			unionPlayerDTO.setScore(0);
//		}
	}
	
	public void quitSession(WebSocketSession session, GameRoom gameRoom, CloseStatus status) {
//		Map<String, Object> map = session.getAttributes();
//		UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
				
//		GameRoom gameRoom = gameRoomService.findRoomById(unionPlayerDTO.getRoomId());
		gameRoom.removeSession(session);
		
		log.info("bye");
		log.info(gameRoom.getSessions());
		
//		return unionPlayerDTO;
	}
	
	public Boolean isPlaying(GameRoom gameRoom) {
		log.info("isPlaying : " + gameRoom.getPlaying());
		log.info("lastGameDTO : " + gameRoom.getLastGameDTO());
		return gameRoom.getPlaying() && !gameRoom.getLastGameDTO().equals(null);
	}
	
	public Boolean isGuest(UnionPlayerDTO player) {
		boolean isGuest = true;
		
		if(player.getStatus() == StatusType.HOST ||
				player.getStatus() == StatusType.OPPONENT) {
			isGuest = false;
		}
			
		
//		switch (player.getStatus()) {
//		case "HOST":
//		case "OPPONENT":
//			isGuest = false;
//			break;
//		}
		return isGuest;
	}
	
	public UnionSettingDTO quitPlayer(UnionPlayerDTO player, GameRoom gameRoom) {
//		GameRoom gameRoom = gameRoomService.findRoomById(player.getRoomId());
		gameRoom.removePlayer(player);
		
		Map<String, Object> jsonMap = preprocessing(MessageType.LEAVE, gameRoom.getRoomId());
		
//		jsonMap.put("type", "LEAVE");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
		
		jsonMap.put("user1", player.getUserId());
		
		UnionSettingDTO unionSettingDTO = postprocessing(jsonMap);
	
		return unionSettingDTO;
	}
	
	// TODO : Resume game functions
	
//	public UnionSettingDTO playerGone(UnionPlayerDTO player, GameRoom gameRoom) {
//		preprocessing();
//		
//		jsonMap.put("type", "QUIT");
//		jsonMap.put("roomId", gameRoom.getRoomId());
//		jsonMap.put("sender", "Setting");
//		jsonMap.put("message", player.getUserEmail() + "가 나가셨습니다. 10초간 기다립니다.");
//		jsonMap.put("countDown", 10);
//		
//		postprocessing();
//		
//		return unionSettingDTO;
//	}
//	
//	public void quitOtherPlayer(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
//		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
//		
//		for(UnionPlayerDTO player : engaged) {
//			if(!session.getAttributes().get("player").equals(player)) {
//				gameRoom.removePlayer(player);
//				break;
//			}
//		}
//	}
}