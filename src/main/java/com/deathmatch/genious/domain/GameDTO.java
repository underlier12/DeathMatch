package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDTO {

	// 메시지 타입 : 입장, 채팅
    public enum MessageType {
        JOIN, TALK, READY, SCORE, UNI, ON, OUT
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
}
