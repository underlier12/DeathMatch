package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionSettingDTO {

	public enum MessageType {
        READY, ROUND, PROBLEM
    }
    private MessageType type; 
    private String roomId; 
    private String sender; 
    private String message;
    private String user1;
    private String user2;
    private int round;
    
    private String[] cards;

}
