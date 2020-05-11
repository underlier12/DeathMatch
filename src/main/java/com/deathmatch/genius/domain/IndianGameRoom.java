package com.deathmatch.genius.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.web.socket.WebSocketSession;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianGameRoom {

	private String roomId;
	//private String roomName;
	private String name;
	private String gameType;
	private String gameId;
    private Boolean playing = false;
	 
	private Set<WebSocketSession> sessions = new HashSet<>(); // 입장한 클라이언트의 정보 보관
	private List<IndianPlayerDTO> players = new LinkedList<>(); //유저 정보를 보관
	
	//@Builder
	//public IndianGameRoom(String roomId, String roomName) {
	//	this.roomId = roomId;
	//	this.roomName = roomName;
	//}
	
	@Builder
	public IndianGameRoom(String gameType,String roomId,String name) {
		this.gameType = gameType;
		this.roomId = roomId;
		this.name = name;
	}
	
	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}
	
	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public void addPlayer(IndianPlayerDTO player) {
		players.add(player);
	}
	
	public void removePlayer(IndianPlayerDTO player) {
		players.remove(player);
	}

}
