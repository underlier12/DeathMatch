package com.deathmatch.genious.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionAnswerDTO;
import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionProblemDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class UnionSettingDAOImpl implements UnionSettingDAO {

	private final ObjectMapper objectMapper;
	
	private String namespace = "com.deathmatch.genious.mapper.UnionSettingMapper";
	private SqlSession sqlSession;
	
	private Map<String, Object> jsonMap;
	private UnionProblemDTO unionProblemDTO;
	private UnionAnswerDTO unionAnswerDTO;
	private JSONObject jsonObject;
	private String jsonString;
	
	public UnionSettingDAOImpl(SqlSession sqlSession) {
		this.objectMapper = new ObjectMapper();
		this.sqlSession = sqlSession;
	}
	
	public void preprocessing() {
		jsonMap = new HashMap<>();
	}
	
	public void postprocessing(Boolean problem) {
		jsonObject = new JSONObject(jsonMap);
		jsonString = jsonObject.toJSONString();
		log.info("jsonString : " + jsonString);

		if(problem) {
			try {
				unionProblemDTO = objectMapper.readValue(jsonString, UnionProblemDTO.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} else {
			try {
				unionAnswerDTO = objectMapper.readValue(jsonString, UnionAnswerDTO.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//	@Override
//	public List<UnionCardDTO> selectAllCard() {
//		return sqlSession.selectList(namespace + ".selectAllCard");
//	}
	
	@Override
	public List<UnionCardDTO> makeUnionProblem() {
		return sqlSession.selectList(namespace + ".makeProblem");
	}

	@Override
	public List<UnionCardDTO> selectUnionProblem(GameRoom gameRoom) {
		List<UnionCardDTO> problemList = new ArrayList<>();
		
		for(int i = 0; i < 9; i++) {
			preprocessing();
			
			jsonMap.put("gameId", gameRoom.getGameId());
			jsonMap.put("round", gameRoom.getRound());
			jsonMap.put("idx", i);
			
			postprocessing(true);
			UnionCardDTO problemEach = sqlSession.selectOne(namespace + ".selectProblemEach", unionProblemDTO);
			problemList.add(problemEach);
		}
		
		return problemList;
	}

	@Override
	public void insertProblem(GameRoom gameRoom, int idx, String cardName) {
		preprocessing();
		
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("round", gameRoom.getRound());
		jsonMap.put("idx", idx);
		jsonMap.put("card", cardName);

//		for(int i = 0; i < 9; i++) {
//			jsonMap.put("card" + Integer.toString(i+1), problem.get(i));
//		}
		
		postprocessing(true);
		
		sqlSession.insert(namespace + ".insertProblem", unionProblemDTO);
	}

	@Override
	public void insertAnswer(GameRoom gameRoom, Set<String> answerSet) {
		
		for(String answer : answerSet) {
			
			preprocessing();
			
			jsonMap.put("gameId", gameRoom.getGameId());
			jsonMap.put("round", gameRoom.getRound());
			jsonMap.put("answer", answer);
			
			postprocessing(false);
			
			sqlSession.insert(namespace + ".insertAnswer", unionAnswerDTO);
		}

	}

}
