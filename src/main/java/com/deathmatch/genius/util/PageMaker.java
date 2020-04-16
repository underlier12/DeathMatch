package com.deathmatch.genius.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// 페이지에 페이징 관련 버튼을 만드는 기능을 하는 클래스
// 여러개의 Page를 의미한다
public class PageMaker {

	private Criteria cri;
	private int totalCount; // 총 방의 개수
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	private int displayPageNum = 5; // 화면에 보여지는 페이지 숫자
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}

	private void calcData() {
		endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);

		startPage = (endPage - displayPageNum) + 1;
		if (startPage <= 0)
			startPage = 1;

		int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}

		prev = startPage == 1 ? false : true;
		next = endPage * cri.getPerPageNum() < totalCount ? true : false;
	}

}
