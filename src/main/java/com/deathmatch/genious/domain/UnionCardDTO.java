package com.deathmatch.genious.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UnionCardDTO {

	public enum ShapeType {
		SUN, MOON, ASTER
	}
	
	public enum ColorType {
		PURPLE, ORANGE, GREEN
	}
	
	public enum BackType {
		WHITE, GRAY, BLACK
	}
	
	private String name;
	private ShapeType shape;
	private ColorType color;
	private BackType background;
	
	public UnionCardDTO() {	}
	
	@Builder
	public UnionCardDTO(String name, ShapeType shape, 
			ColorType color, BackType background) {
		this.name = name;
		this.shape = shape;
		this.color = color;
		this.background = background;
	}
	
}
