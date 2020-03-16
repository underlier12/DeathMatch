package com.deathmatch.genious.dao;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.deathmatch.genious.domain.UnionGameDTO;

public interface UnionDealerDAO {

	// check whether in answers or not
	public Boolean checkAnswer(UnionGameDTO gameDTO, GameRoom gameRoom);
	
	// check whether in correct submitted answers or not
	public Boolean checkCorrectSubmittedAnswer(UnionGameDTO gameDTO, GameRoom gameRoom);
	
	// count answers
	public int countAnswer(UnionGameDTO gameDTO, GameRoom gameRoom);
	
	// count correct submitted answers
	public int countCorrectSubmittedAnswer(UnionGameDTO gameDTO, GameRoom gameRoom);
	
	// insert submitted answer
	public void insertSubmittedAnswer(UnionDealerDTO unionDealerDTO);
}
