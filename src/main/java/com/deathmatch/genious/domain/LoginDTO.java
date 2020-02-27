package com.deathmatch.genious.domain;

public class LoginDTO {

	private String userEmail;
	private String pw;
	private boolean useCookie;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public boolean isUseCookie() {
		return useCookie;
	}

	public void setUseCookie(boolean useCookie) {
		this.useCookie = useCookie;
	}

	@Override
	public String toString() {
		return "LoginDTO{" + "userEmail='" + userEmail + '\'' + ", pw='" + pw + '\'' + ", useCookie=" + useCookie + '}';
	}

}
