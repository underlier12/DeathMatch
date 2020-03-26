package com.deathmatch.genious.util;

// 검색기준, 분류 기준
// 하나의 페이지를 의미
public class Criteria {
	
	private int page; //현재 페이지 번호
	private int perPageNum; //페이지당 보여지는 게시물의 개수
	
	public Criteria(){
		this.page = 1;
		this.perPageNum = 10;
	}
	
	public void setPage(int page) {
		if(page <=0) {
			this.page = 1;
		}else{
			this.page = page;
		}
	}
	
	public void setPerPageNum(int pageCount) {
		int cnt = this.perPageNum;
		if(pageCount != cnt) {
			this.perPageNum = cnt;
		}else {
			this.perPageNum = pageCount;
		}
	}
	
	public int getPage() {
		return page;
	}
	
	// 게시글 시작 행 번호 = (현재 페이지 번호 -1) * 페이지당 보여줄 게시글 갯수
	public int getPageStart() {
		return (this.page -1 ) * perPageNum;
	}
	
	// mybatis SQL Mapper
	public int getPerPageNum() {
		return this.perPageNum;
	}
	
	@Override
	public String toString() {
		return "Criteria [page =" + page + ", " + "perPageNum= "+ perPageNum +"]";
	}
	
}
