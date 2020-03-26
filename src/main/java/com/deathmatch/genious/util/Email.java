package com.deathmatch.genious.util;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Component
public class Email {
	
	private String subject;
	private String content;
	private String receiver;
	
}
