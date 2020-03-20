package com.deathmatch.genious.domain;

import java.util.Date;

import com.mysql.cj.log.Log;

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
	private int score;
	private String phone;
	private Date join_date;
	
	public UserDTO(String userEmail, String userId, String pw, String name, int score, String phone, Date join_date) {
		this.userEmail = userEmail;
		this.userId = userId;
		this.pw = pw;
		this.name = name;
		this.score = score;
		this.phone = phone;
		this.join_date = join_date;
	}
	
	public UserDTO(String userEmail, String pw) {
		this.userEmail = userEmail;
		this.pw = pw;
	}
	
}
