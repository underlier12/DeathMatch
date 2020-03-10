package com.deathmatch.genious.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDTO {

	private String userEmail;
	private String pw;
	private boolean useCookie;
	
}
