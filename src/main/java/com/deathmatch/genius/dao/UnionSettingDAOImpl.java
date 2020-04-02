package com.deathmatch.genius.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.UnionCardDTO;
import com.deathmatch.genius.domain.UnionDatabaseDTO;

@Repository
public class UnionSettingDAOImpl implements UnionSettingDAO {
	
	private String namespace = "com.deathmatch.genious.mapper.UnionSettingMapper";
	private SqlSession sqlSession;
	
	public UnionSettingDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public List<UnionCardDTO> makeUnionProblem() {
		return sqlSession.selectList(namespace + ".makeProblem");
	}

	@Override
	public List<UnionCardDTO> selectUnionProblem(UnionDatabaseDTO dbDTO) {
		List<UnionCardDTO> problemList = new ArrayList<>();
		for(int i = 0; i < 9; i++) {
			dbDTO.setIdx(i);
			UnionCardDTO problemEach = 
					sqlSession.selectOne(namespace + ".selectProblemEach", dbDTO);
			problemList.add(problemEach);
		}
		return problemList;
	}

	@Override
	public void insertProblem(UnionDatabaseDTO dbDTO, List<String> problemCardNames) {
		for(int i = 0; i < 9; i++) {
			dbDTO.setIdx(i);
			dbDTO.setCard(problemCardNames.get(i));
			sqlSession.insert(namespace + ".insertProblem", dbDTO);
		}
	}

	@Override
	public void insertAnswer(UnionDatabaseDTO dbDTO, Set<String> answerSet) {
		for(String answer : answerSet) {
			dbDTO.setCard(answer);
			sqlSession.insert(namespace + ".insertAnswer", dbDTO);
		}
	}

}
