package com.deathmatch.genious.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;

@Repository
public class UnionSettingDAOImpl implements UnionSettingDAO {

	private String namespace = "com.deathmatch.genious.mapper.UnionSettingMapper";
	private SqlSession sqlSession;
	
	public UnionSettingDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public List<UnionCardDTO> selectAllCard() {
		return sqlSession.selectList(namespace + ".selectAllCard");
	}

	@Override
	public List<UnionCardDTO> selectUnionProblem(GameRoom gameRoom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertProblem(GameRoom gameRoom, List<UnionCardDTO> problem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inserAnswer(GameRoom gameRoom, List<UnionCardDTO> answer) {
		// TODO Auto-generated method stub

	}

}
