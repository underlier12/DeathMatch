package com.deathmatch.genius.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeBoardDTO {
	
	private int bno;
	private String userId;
	private String title;
	private String content;
	private LocalDate regdate;
	private int hit;
	private int pw; // 비밀 번호
	
	@Builder
	public NoticeBoardDTO(int bno,String userId,String title,String content,LocalDate regdate,
			int hit,Integer ref,int step,int depth,int pw){
		this.bno = bno;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.regdate = regdate;
		this.hit = hit;
		this.pw = pw;
	}
}
