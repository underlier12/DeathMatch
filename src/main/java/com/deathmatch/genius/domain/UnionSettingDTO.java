package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionSettingDTO {

	public enum MessageType {
        LOAD, JOIN, READY, PROBLEM, LEAVE
    }
    private MessageType type; 
    private String roomId; 
    private String sender; 
    private String message;
    private String user1;
    private String user2;
    private String[] cards;

}
