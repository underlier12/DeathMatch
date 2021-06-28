package com.deathmatch.genius.domain;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {

	private String userEmail;
	private String userId;
	private String pw;
	private String name;
	private Date join_date;
	private int auth;
	private String role;
	
	@Builder
	public UserDTO(String userEmail, String userId, String pw, String name, Date join_date,int auth,String role) {
		this.userEmail = userEmail;
		this.userId = userId;
		this.pw = pw;
		this.name = name;
		this.join_date = join_date;
		this.auth = auth;
		this.role = role;
	}
	
	
}
