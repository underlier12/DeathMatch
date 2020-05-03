package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordDTO {

	private String gameId;
	private String gameType;
	private String userId;
	private String winLose;
	private int score;
	
}
