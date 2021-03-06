package com.deathmatch.genius.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private String gameType;
    private String gameId;
    private Boolean playing; // = false;
    private int totalRound; // = 3;
    private int round;
    private int pass;
    
    private List<UnionPlayerDTO> engaged = new ArrayList<>();
    private Set<WebSocketSession> sessions = new HashSet<>();
    
    @Builder
    public GameRoom(String gameType, String roomId, String name) {
    	this.gameType = gameType;
        this.roomId = roomId;
        this.name = name;
        
        playing = false;
        totalRound = 3;
    }
    
    public void addSession(WebSocketSession session) {
    	sessions.add(session);
    }
    
    public void removeSession(WebSocketSession session) {
    	sessions.remove(session);
    }
    
    public void addPlayer(UnionPlayerDTO player) {
    	engaged.add(player);
    }
    
    public void removePlayer(UnionPlayerDTO player) {
    	engaged.remove(player);
    }

}
