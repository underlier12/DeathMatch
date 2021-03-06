package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.deathmatch.genius.domain.IndianGameDTO;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.IndianPlayerDTO;
import com.deathmatch.genius.domain.IndianPlayerDTO.StatusType;
import com.deathmatch.genius.domain.IndianServiceDTO;
import com.deathmatch.genius.domain.IndianServiceDTO.MessageType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class IndianService {

	private final ObjectMapper objectMapper;
	private final IndianDealerService dealService;

	public IndianService(ObjectMapper objectMappper, IndianDealerService dealService) {
		this.objectMapper = objectMappper;
		this.dealService = dealService;
	}

	public void handleActions(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		log.info("Type : " + indianGameDTO.getType());
		switch (indianGameDTO.getType()) {
		case JOIN:
			joinUser(session, indianGameDTO, indianRoom);
			break;
		case TALK:
			sendMessageAll(indianRoom.getSessions(), indianGameDTO);
			break;
		case READY:
			readyAct(session, indianGameDTO, indianRoom);
			break;
		case BETTING:
			bettingAct(session, indianGameDTO, indianRoom);
			break;
		case GIVEUP:
			giveUpAct(session, indianGameDTO, indianRoom);
			break;
		case ROUND:
			nextRound(session, indianGameDTO, indianRoom);
			break;
		case NEXTDRAW:
			drawNextRound(session, indianGameDTO, indianRoom);
			break;
		}
	}

	/* From Server to Client need to Parsing Json */

	public Map<String, Object> convertMap(MessageType type, String roomId) {
		Map<String, Object> jsonMap = new HashMap<>();

		jsonMap.put("type", type.toString());
		jsonMap.put("roomId", roomId);

		return jsonMap;
	}
	
	public IndianServiceDTO processing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		IndianServiceDTO indianServiceDTO = null;
		log.info("jsonString " + jsonString);

		try {
			indianServiceDTO = objectMapper.readValue(jsonString, IndianServiceDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indianServiceDTO;
	}

	public boolean readyCheck(IndianGameRoom indianRoom) {
		boolean flag = false;
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		if (players.size() == 2) {
			flag = players.get(1).getReady();
		}
		return players.get(0).getReady() && flag;
	}

	public IndianServiceDTO readyUser(WebSocketSession session, IndianGameDTO indianGameDTO,
			IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = convertMap(MessageType.READY, indianRoom.getRoomId());
		IndianPlayerDTO player = (IndianPlayerDTO) session.getAttributes().get("player");
		player.setReady(true);
	
		jsonMap.put("message", indianGameDTO.getSender() + "님이 준비하셨습니다 ! ");
		jsonMap.put("player", indianGameDTO.getSender());
		IndianServiceDTO indianServiceDTO = processing(jsonMap);
		return indianServiceDTO;
	}
	
	public IndianServiceDTO allReady(IndianGameRoom indianRoom, IndianGameDTO indianGameDTO) {
		Map<String, Object> jsonMap = convertMap(MessageType.READY, indianRoom.getRoomId());
		log.info("allReady Sender : " + indianGameDTO.getSender());
		
		jsonMap.put("message", "플레이어가 모두 준비하였습니다! 게임을 시작합니다 ");
		jsonMap.put("firstTurn", dealService.nextTurn(indianRoom) + "님의 차례입니다 ");
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		
		IndianServiceDTO indianServiceDTO = processing(jsonMap);
		return indianServiceDTO;
	}
	

	public void register(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		StatusType status = decideStatus(indianRoom);

		IndianPlayerDTO player = IndianPlayerDTO.builder()
				.userId(indianGameDTO.getSender())
				.roomId(indianRoom.getRoomId())
				.ready(false)
				.turn(false)
				.status(status)
				.chip(30)
				.betChip(0)
				.build();
		log.info("register User: " + player.toString());

		Map<String, Object> map = session.getAttributes();
		map.put("player", player);
		indianRoom.addPlayer(player);
	}

	public StatusType decideStatus(IndianGameRoom indianRoom) {
		StatusType status;

		if (indianRoom.getPlayers().size() == 0)
			status = StatusType.HOST;
		else if (indianRoom.getPlayers().size() == 1)
			status = StatusType.OPPONENT;
		else
			status = StatusType.GUEST;

		return status;
	}
	
	public void readyAct(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		sendMessageAll(indianRoom.getSessions(), readyUser(session, indianGameDTO, indianRoom));
		if (readyCheck(indianRoom)) {
			startGame(indianRoom);
			dealService.makeCardDeck();
			sendMessageAll(indianRoom.getSessions(), dealService.startRound(indianRoom));
			sendMessageAll(indianRoom.getSessions(), allReady(indianRoom, indianGameDTO));
		}
	}

	public void joinUser(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = convertMap(MessageType.JOIN, indianGameDTO.getRoomId());
		indianRoom.addSession(session);
		loadGame(session, indianRoom);
		register(session, indianGameDTO, indianRoom);
		
		jsonMap.put("message", indianGameDTO.getSender() + "님이 방에 입장하셨습니다! ");
		jsonMap.put("player", indianGameDTO.getSender());
		jsonMap.put("checkPlayer", indianRoom.getPlayers().get(0).getUserId());
		
		IndianServiceDTO indianServiceDTO = processing(jsonMap);
		sendMessageAll(indianRoom.getSessions(), indianServiceDTO);
	}

	public void bettingAct(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		String player = indianGameDTO.getSender();
		log.info("sender: " + player);
		if (indianGameDTO.getPlayer1BetChip() == indianGameDTO.getPlayer2BetChip()) {
			sendMessageAll(indianRoom.getSessions(), dealService.resultRound(indianGameDTO, indianRoom));
			log.info("Result Round sendMessageAll");
		}
		sendMessageAll(indianRoom.getSessions(), dealService.whoseTurn(indianRoom, indianGameDTO));
	}

	public void nextRound(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		sendMessageAll(indianRoom.getSessions(), dealService.nextRound(indianRoom));
	}

	public void drawNextRound(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		sendMessageAll(indianRoom.getSessions(), dealService.drawNextRound(indianRoom, indianGameDTO));
	}

	public void giveUpAct(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		sendMessageAll(indianRoom.getSessions(), dealService.giveUpRound(indianGameDTO, indianRoom));
	}

	public void endGame(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		sendMessageAll(indianRoom.getSessions(), dealService.endGame(indianRoom));
	}

	Queue<Object> playerQueue = new LinkedList<>();

	public IndianServiceDTO loadPlayer(IndianPlayerDTO player, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = convertMap(MessageType.LOAD, indianRoom.getRoomId());
		jsonMap.put("message", "PLAYER");
		jsonMap.put("player", player.getUserId());

		IndianServiceDTO indianServiceDTO = processing(jsonMap);
		return indianServiceDTO;
	}

	public void loadGame(WebSocketSession session, IndianGameRoom indianRoom) {
		log.info("playerNum : " + indianRoom.getPlayers().size());
		switch (indianRoom.getPlayers().size()) {
		case 2:
			IndianServiceDTO getPlayer1 = loadPlayer(indianRoom.getPlayers().get(1), indianRoom);
			playerQueue.add(getPlayer1);
		case 1:
			IndianServiceDTO getPlayer2 = loadPlayer(indianRoom.getPlayers().get(0), indianRoom);
			playerQueue.add(getPlayer2);
			break;
		}
		loadPlayer(session, indianRoom);
	}

	public void loadPlayer(WebSocketSession session, IndianGameRoom indianRoom) {
		while (!playerQueue.isEmpty()) {
			sendMessage(session, playerQueue.poll());
		}
	}

	public void quitSession(WebSocketSession session, IndianGameRoom indianRoom, CloseStatus status) {
		indianRoom.removeSession(session);
		log.info("session close");
	}

	public void afterConnectionClosed(WebSocketSession session, IndianPlayerDTO player, IndianGameRoom indianRoom,
			CloseStatus status) {
		quitSession(session, indianRoom, status);
		log.info("player Status" + player.getStatus());

		if (isPlaying(indianRoom) && !isGuest(player)) {
			sendMessageAll(indianRoom.getSessions(), quitPlayer(player, indianRoom));
			sendMessageAll(indianRoom.getSessions(), dealService.stopGame(indianRoom));
			dealService.resetGame(indianRoom);
		} else if (!isGuest(player)) {
			sendMessageAll(indianRoom.getSessions(), quitPlayer(player, indianRoom));
		}
	}

	public Boolean isGuest(IndianPlayerDTO player) {
		return player.getStatus() == StatusType.GUEST;
	}

	public Boolean isPlaying(IndianGameRoom indianRoom) {
		return indianRoom.getPlaying();
	}

	public void startGame(IndianGameRoom indianRoom) {
		indianRoom.setGameId(UUID.randomUUID().toString());
		indianRoom.setPlaying(true);
	}

	public IndianServiceDTO quitPlayer(IndianPlayerDTO player, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = convertMap(MessageType.LEAVE, indianRoom.getRoomId());
		indianRoom.removePlayer(player);

		jsonMap.put("message", player.getUserId() + "님이 퇴장했습니다.");
		jsonMap.put("player", player.getUserId());
		IndianServiceDTO indianServiceDTO = processing(jsonMap);
		return indianServiceDTO;
	}

	public Boolean isEmptyRoom(IndianGameRoom indianRoom) {
		return indianRoom.getSessions().size() == 0;
	}

	public <T> void sendMessageAll(Set<WebSocketSession> sessions, T message) {
		log.info("sendMessageAll");
		sessions.parallelStream().forEach(session -> sendMessage(session, message));
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
