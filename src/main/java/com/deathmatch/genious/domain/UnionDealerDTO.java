package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionDealerDTO {

    public enum MessageType {
        JOIN, TALK, READY, UNI, ON, OUT
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private int countDown;
    private int score;
}
