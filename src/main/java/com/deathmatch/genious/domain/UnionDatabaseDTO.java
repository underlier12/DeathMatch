package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionDatabaseDTO {

	private String gameId;
	private int round;
	private int idx;
	private String card;
}
