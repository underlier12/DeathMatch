package com.deathmatch.genious.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.deathmatch.genious.domain.GameRoom;
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
	private JSONObject jsonObject;
	private String jsonString;
	
	public UnionSettingDAOImpl(SqlSession sqlSession) {
		this.objectMapper = new ObjectMapper();
		this.sqlSession = sqlSession;
	}
	
	public void preprocessing() {
		jsonMap = new HashMap<>();
	}
	
	public void postprocessing() {
		jsonObject = new JSONObject(jsonMap);
		jsonString = jsonObject.toJSONString();
		log.info("jsonString : " + jsonString);

		try {
			unionProblemDTO = objectMapper.readValue(jsonString, UnionProblemDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertProblem(GameRoom gameRoom, List<String> problem) {
		preprocessing();
		
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("round", gameRoom.getRound());
		
		for(int i = 0; i < 9; i++) {
			jsonMap.put("card" + Integer.toString(i+1), problem.get(i));
		}
		
		postprocessing();
		
		sqlSession.insert(namespace + ".insertProblem", unionProblemDTO);
	}

	@Override
	public void inserAnswer(GameRoom gameRoom, String answer) {
		// TODO Auto-generated method stub

	}

}
