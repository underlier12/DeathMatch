package com.deathmatch.genius.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SuggestionReplyDTO {
	
	private int rno;
	private int bno;
	private String content;
	private String userId;
	private LocalDate regdate;
}
