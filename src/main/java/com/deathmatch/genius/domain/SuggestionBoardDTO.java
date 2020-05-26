package com.deathmatch.genius.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SuggestionBoardDTO {
	
	private int bno;
	private String userId;
	private String title;
	private String content;
	private LocalDate regdate;
	private int hit;
	private Integer ref; // 그룹 번호
	private int step; // 순서
	private int depth; // 단계
	private int pw; // 비밀 번호
	
	@Builder
	public SuggestionBoardDTO(int bno,String userId,String title,String content,LocalDate regdate,
			int hit,Integer ref,int step,int depth,int pw){
		this.bno = bno;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.regdate = regdate;
		this.hit = hit;
		this.ref = ref;
		this.step = step;
		this.depth = depth;
		this.pw = pw;
	}
}
