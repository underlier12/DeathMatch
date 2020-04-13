package com.deathmatch.genius.domain;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.socket.WebSocketSession;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianGameRoom {

	private String roomId;
	private String roomName;
	private Set<WebSocketSession> sessions = new HashSet<>(); // 입장한 클라이언트의 정보 보관
	
	@Builder
	public IndianGameRoom(String roomId, String roomName) {
		this.roomId = roomId;
		this.roomName = roomName;
	}
	
	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}
	
	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}
 
}
