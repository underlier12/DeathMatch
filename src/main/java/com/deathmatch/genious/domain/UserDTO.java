package com.deathmatch.genious.domain;

import java.util.Date;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getJoin_date() {
        return join_date;
    }

    public void setJoin_date(Date join_date) {
        this.join_date = join_date;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userEmail='" + userEmail + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", phone='" + phone + '\'' +
                ", join_date=" + join_date +
                '}';
    }


}
