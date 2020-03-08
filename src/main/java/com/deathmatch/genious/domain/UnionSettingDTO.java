package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionSettingDTO {

	public enum MessageType {
        PROBLEM
    }
    private MessageType type; 
    private String roomId; 
    private String sender; 
    
    private String card1;
    private String card2;
    private String card3;
    private String card4;
    private String card5;
    private String card6;
    private String card7;
    private String card8;
    private String card9;

}
