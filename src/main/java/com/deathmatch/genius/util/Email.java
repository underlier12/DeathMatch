package com.deathmatch.genius.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class Email {
	
	private String subject;
	private String content;
	private String receiver;
	
	@Builder
	public Email(String subject, String content, String receiver) {
		this.subject = subject;
		this.content = content;
		this.receiver = receiver;
	}
	
	
	
}
