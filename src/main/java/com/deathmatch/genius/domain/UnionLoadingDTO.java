package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionLoadingDTO {

	public enum MessageType {
        LOAD
    }
    private MessageType type; 
    private String roomId; 
    private String sender; 
    private String message;
    private String user;
    private String[] set;
    private int score;

}
