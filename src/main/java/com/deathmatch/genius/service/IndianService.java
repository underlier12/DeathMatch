package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genius.dao.IndianSettingDAOImpl;
import com.deathmatch.genius.domain.IndianCardDTO;
import com.deathmatch.genius.domain.IndianGameDTO;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

/**
 * 
 * ChatService 실제 내용을 구현한다
 *
 */
@Log4j
@Service
public class IndianService {

	private final ObjectMapper objectMapper;
	private final IndianSettingDAOImpl indianDao;

	public IndianService(ObjectMapper objectMappper,IndianSettingDAOImpl indianDao) {
		this.objectMapper = objectMappper;
		this.indianDao = indianDao;
	}

	public void handleActions(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom chatRoom) {

		log.info("Type : " + indianGameDTO.getType());
		switch (indianGameDTO.getType()) {
		case JOIN:
			joinUser(session, indianGameDTO, chatRoom);
			break;
		case TALK:
			sendMessageAll(chatRoom.getSessions(), indianGameDTO);
			break;
		}
	}

	public void joinUser(WebSocketSession session, IndianGameDTO indianGameDTO, IndianGameRoom chatRoom) {
		chatRoom.addSession(session);
		log.info("Session: " + session + " Join User: " + indianGameDTO.getSender());
		indianGameDTO.setMessage(indianGameDTO.getSender() + "님이 입장했습니다");
	}
	
	public void quitSession(WebSocketSession session,IndianGameRoom chatRoom) {
		chatRoom.removeSession(session);
		log.info("session close");
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
	
	public List<IndianCardDTO> getList(){
		List<IndianCardDTO> cardDeck = indianDao.problemList();
		return cardDeck;
	}
}
