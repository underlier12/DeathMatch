package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnionService {

	private ObjectMapper objectMapper = new ObjectMapper();

	public <T> void readyCheck(Set<WebSocketSession> sessions, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== readyCheck(Set) =========");
    	System.out.println();
    	
	}
	
	public <T> void uniCheck(WebSocketSession session, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== uniCheck(WebSocketSession) =========");
    	System.out.println();
		
    	try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void onCheck(WebSocketSession session, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== onCheck(WebSocketSession) =========");
    	System.out.println();
    	
    	try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void countScore(WebSocketSession session, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== countScore(Set) =========");
    	System.out.println();
    	
    	try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void sendMessage(WebSocketSession session, T message) {
		
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== sendMessage(WebSocketSession, T) =========");
		System.out.println();
		
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
//	public void handleActionOthers(WebSocketSession session, GameDTO gameDTO) {
//		
//		System.out.println("=========== Enter UnionService =========");
//    	System.out.println("=========== sendMessage(WebSocketSession, T) =========");
//    	System.out.println();
//    	
//    	
//    	
//	}
//	
}
