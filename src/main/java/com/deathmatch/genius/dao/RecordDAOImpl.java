package com.deathmatch.genius.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.RecordDTO;

@Repository
public class RecordDAOImpl implements RecordDAO {
	
	private final String namespace = "com.deathmatch.genius.mapper.RecordMapper";
	private final SqlSession sqlSession;
	
	public RecordDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void insertHistory(RecordDTO recordDTO) {
		sqlSession.insert(namespace + ".insertHistory", recordDTO);
	}

	@Override
	public List<RecordDTO> selectAllRecord(String userId) {
		return sqlSession.selectList(namespace + ".selectBattleByUserId", userId);
	}
	
	@Override
	public List<RecordDTO> selectRecordByGameType(String gameType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecordDTO selectOpponentRecord(RecordDTO recordDTO) {
		return sqlSession.selectOne(namespace + ".selectOpponentByGameId", recordDTO);
	}

}
