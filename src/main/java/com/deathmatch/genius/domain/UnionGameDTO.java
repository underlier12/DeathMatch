package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionGameDTO {
    public enum MessageType {
        JOIN, READY, UNI, ON, TIMEUP,ROUND, TALK
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String gameId;
    private int round;
    private int pass;
}
