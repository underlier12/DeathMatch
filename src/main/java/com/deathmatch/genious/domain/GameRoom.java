package com.deathmatch.genious.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRoom {
    private String roomId;
    private String name;
    private String gameId;
    private int round = 0;
    private Map<String, Boolean> readyUser = new LinkedHashMap<String, Boolean>();
    private Set<WebSocketSession> sessions = new HashSet<>();
//    private Map<String, UnionCardDTO> problemMap = new LinkedHashMap<>();
    private List<UnionCardDTO> problemList = new ArrayList<>();
	private Set<UnionAnswerDTO> answerSet = new HashSet<>();
	private Set<UnionAnswerDTO> submitedAnswerSet = new HashSet<>();


    @Builder
    public GameRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
    
    // Guenho 작성
    
    private String player1;
	private String player2;
	
    @Builder
    public GameRoom(String roomId,String name,String player1,String player2) {
    	this.roomId = roomId;
    	this.name = name;
    	this.player1 = player1;
    	this.player2 = player2;
    }
   

}
