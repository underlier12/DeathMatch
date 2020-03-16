package com.deathmatch.genious.domain;

import java.util.HashSet;
import java.util.LinkedHashMap;
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
    private String gameType;
    private String gameId;
    private int totalRound = 3;
    private int round = 0;
    private Map<String, Boolean> readyUser = new LinkedHashMap<String, Boolean>();
    private Set<WebSocketSession> sessions = new HashSet<>();
    
    @Builder
    public GameRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

}
