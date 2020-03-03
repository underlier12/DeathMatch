package com.deathmatch.genious.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UnionAnswerDTO {

	private UnionCardDTO card1;
	private UnionCardDTO card2;
	private UnionCardDTO card3;

	@Builder
	public UnionAnswerDTO(UnionCardDTO card1
			, UnionCardDTO card2, UnionCardDTO card3) {
		
		this.card1 = card1;
		this.card2 = card2;
		this.card3 = card3;
	}
}
