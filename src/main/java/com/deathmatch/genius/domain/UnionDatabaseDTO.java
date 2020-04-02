package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionDatabaseDTO {

	private String gameId;
	private String card;
	private int round;
	private int idx;
}
