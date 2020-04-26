package com.deathmatch.genius.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.UnionDatabaseDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class UnionLoadingDAOImpl implements UnionLoadingDAO {
	
	private String namespace = "com.deathmatch.genius.mapper.UnionLoadingMapper";
	private SqlSession sqlSession;
	
	public UnionLoadingDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<String> selectUnionProblemCardNames(UnionDatabaseDTO dbDTO) {
		List<String> problem; // = new ArrayList<>();
		
		problem = sqlSession.selectList(namespace + ".selectProblem", dbDTO);
//		for(int i = 0; i < 9; i++) {
//			dbDTO.setIdx(i);
//			String cardName
//					= sqlSession.selectOne(namespace + ".selectProblemEach");
//			problem.add(cardName);
//		}
		log.info("problem : " + problem.toString());
		
		return problem;
	}

	@Override
	public List<String> selectUnionAnswerSheet(UnionDatabaseDTO dbDTO) {
		List<String> answerSheet = new ArrayList<>();
		
		answerSheet = sqlSession.selectList(namespace + ".selectAnswerSheet");
		log.info("answerSheet : " + answerSheet.toString());
		
		return answerSheet;
	}

}
