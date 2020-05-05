package com.deathmatch.genius.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.RecordDTO;

@Repository
public class RecordDAOImpl implements RecordDAO {
	
	private String namespace = "com.deathmatch.genius.mapper.RecordMapper";
	private SqlSession sqlSession;
	
	public RecordDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void insertHistory(RecordDTO recordDTO) {
		sqlSession.insert(namespace + ".insertHistory", recordDTO);
	}

	@Override
	public List<RecordDTO> selectAllRecord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RecordDTO> selectRecordByGameId(String gameType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecordDTO selectOpponentRecord(RecordDTO recordDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
