package com.deathmatch.genious.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

	private String userEmail;
	private String pw;
	private String name;
	private int score;
	private String phone;
	private Date join_date;

	public UserDTO() {

	}

	public UserDTO(String userEmail, String pw, String name, int score, String phone, Date join_date) {
		this.userEmail = userEmail;
		this.pw = pw;
		this.name = name;
		this.score = score;
		this.phone = phone;
		this.join_date = join_date;
	}

}
