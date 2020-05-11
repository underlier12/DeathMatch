package com.deathmatch.genius.domain;

import java.sql.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SuggestionBoardDTO {
	
	private int bno;
	private String userId;
	private String title;
	private String content;
	private Date regdate;
	private int hit;
	private int ref; // 그룹 번호
	private int step; // 순서
	private int depth; // 단계
	private int pno; // 부모 번호
	private int pw; // 비밀 번호
	
	@Builder
	public SuggestionBoardDTO(int bno,String userId,String title,String content,Date regdate,
			int hit,int ref,int step,int depth,int pno, int pw){
		this.bno = bno;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.regdate = regdate;
		this.hit = hit;
		this.ref = ref;
		this.step = step;
		this.depth = depth;
		this.pno = pno;
		this.pw = pw;
	}
}
