package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionGameDTO {
    public enum MessageType {
        JOIN, TALK, READY, SCORE, UNI, ON, OUT, TIMEUP
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String gameId;
    private int round;
}
