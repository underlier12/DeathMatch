package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDTO {
    public enum MessageType {
        JOIN, TALK, READY, SCORE, UNI, ON, OUT
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
