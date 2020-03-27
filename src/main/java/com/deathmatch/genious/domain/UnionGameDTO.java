package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionGameDTO {
    public enum MessageType {
        JOIN, READY, UNI, ON, TIMEUP
//        TALK, SCORE, OUT, DIE
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String gameId;
    private int round;
}
