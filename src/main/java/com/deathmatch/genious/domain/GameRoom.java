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
    private List<UnionCardDTO> problemList = new ArrayList<>();
    private Set<String> answerSet = new HashSet<>();
    private Set<String> submitedAnswerSet = new HashSet<>();

    @Builder
    public GameRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

}
