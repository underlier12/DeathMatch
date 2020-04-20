package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genius.dao.IndianSettingDAO;
import com.deathmatch.genius.domain.IndianCardDTO;
import com.deathmatch.genius.domain.IndianDealerDTO;
import com.deathmatch.genius.domain.IndianDealerDTO.MessageType;
import com.deathmatch.genius.domain.IndianGameDTO;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.IndianPlayerDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class IndianDealerService {

	private final IndianSettingDAO indianDAO;
	private final ObjectMapper objectMapper;

	public IndianDealerService(IndianSettingDAO indianDAO, ObjectMapper objectMapper) {
		this.indianDAO = indianDAO;
		this.objectMapper = objectMapper;
	}

	public Map<String, Object> convertMap(MessageType type, String roomId) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("type", type.toString());
		jsonMap.put("roomId", roomId);
		jsonMap.put("sender", "Dealer");

		return jsonMap;
	}

	public IndianDealerDTO processing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		IndianDealerDTO indianDealerDTO = null;
		log.info("jsonString : " + jsonString);

		try {
			indianDealerDTO = objectMapper.readValue(jsonString, IndianDealerDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indianDealerDTO;
	}

	/* Make Problem */

	List<IndianCardDTO> cardDeck = new ArrayList<>();
	String[] cardArr;
	int cardIndex = 0;

	public void makeCardDeck() {
		cardDeck = indianDAO.problemList();
	}

	public String[] drawCard(List<IndianCardDTO> cardDeck) {
		cardArr = new String[2];
		int startIdx = cardIndex;
		cardIndex += 2;
		cardArr[0] = cardDeck.get(startIdx).getCardNum();
		cardArr[1] = cardDeck.get(startIdx + 1).getCardNum();
		return cardArr;
	}

	public IndianDealerDTO draw(String[] cardArr, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = convertMap(MessageType.DRAW, indianRoom.getRoomId());
		makeCardDeck();
		cardArr = drawCard(cardDeck);
		jsonMap.put("card1", cardArr[0]);
		jsonMap.put("card2", cardArr[1]);
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("chip1", indianRoom.getPlayers().get(0).getChip());
		jsonMap.put("chip2", indianRoom.getPlayers().get(1).getChip());
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public IndianDealerDTO drawAct(IndianGameRoom indianRoom) {
		makeCardDeck();
		IndianDealerDTO getCard = draw(cardArr, indianRoom);
		log.info("drawAct");
		return getCard;
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
