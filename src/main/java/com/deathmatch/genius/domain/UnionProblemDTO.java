package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionProblemDTO {

	private String gameId;
	private int round;
	private int idx;
	private String card;
}
