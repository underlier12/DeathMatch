package com.deathmatch.genius.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.UnionDealerDTO;
import com.deathmatch.genius.domain.UnionGameDTO;

@Repository
public class UnionDealerDAOImpl implements UnionDealerDAO {

	private String namespace = "com.deathmatch.genius.mapper.UnionDealerMapper";
	private SqlSession sqlSession;
	
	public UnionDealerDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public UnionGameDTO preprocessing(UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameDTO.setGameId(gameRoom.getGameId());
		gameDTO.setRound(gameRoom.getRound());
		
		return gameDTO;
	}

	@Override
	public Boolean checkAnswer(UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameDTO = preprocessing(gameDTO, gameRoom);
		int check = sqlSession.selectOne(namespace + ".checkAnswer", gameDTO);
		
		return (check == 1) ? true : false;
	}

	@Override
	public Boolean checkCorrectSubmittedAnswer(UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameDTO = preprocessing(gameDTO, gameRoom);
		int check = sqlSession.selectOne(namespace + ".checkCorrectSubmittedAnswer", gameDTO);
		
		return (check == 1) ? true : false;
	}

	@Override
	public int countAnswer(UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameDTO = preprocessing(gameDTO, gameRoom);
		
		return sqlSession.selectOne(namespace + ".countAnswer", gameDTO);
	}

	@Override
	public int countCorrectSubmittedAnswer(UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameDTO = preprocessing(gameDTO, gameRoom);
		
		return sqlSession.selectOne(namespace + ".countCorrectSubmittedAnswer", gameDTO);
	}

	@Override
	public void insertSubmittedAnswer(UnionDealerDTO unionDealerDTO) {
		sqlSession.insert(namespace + ".insertSubmittedAnswer", unionDealerDTO);
	}

}
