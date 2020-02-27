package com.deathmatch.genious.service;

import org.springframework.stereotype.Service;

import com.deathmatch.genious.dao.UserDAO;
import com.deathmatch.genious.domain.LoginDTO;
import com.deathmatch.genious.domain.UserDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class UserServiceImpl implements UserService {

	private final UserDAO userDAO;

	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public void insertMember(UserDTO userDTO) {
		userDAO.insertMember(userDTO);
	}

	@Override
	public UserDTO login(LoginDTO loginDTO) {
		return userDAO.login(loginDTO);
	}

	@Override
	public int deleteMember() {
		return 0;
	}

	@Override
	public int deleteAllMember() {
		return 0;
	}

	@Override
	public int modifyPw(UserDTO userDTO) {
		return 0;
	}

	@Override
	public UserDTO findId(UserDTO userDTO) {
		return null;
	}

	@Override
	public UserDTO findPw(UserDTO userDTO) {
		return null;
	}

	// 카카오 회원이 없으면 새로 등록, 있으면 해당 정보 값을 가져온다
	@Override
	public UserDTO kakaoLogin(UserDTO userDTO) {
		UserDTO user = userDAO.searchMember(userDTO);
		log.info("현재 아이디 : " + userDTO.getUserEmail());
		log.info("데이터 베이스 조회 아이디 : " + user.getUserEmail());
		if (userDTO.getUserEmail().equals(user.getUserEmail())) {
			// 있으면 기존 정보를 불러온다
			userDTO = userDAO.selectKakaoMember(userDTO);
		} else {
			// 없으면 신규 가입한다
			userDAO.insertKakaoMember(userDTO);
		}
		return userDTO;
	}

	@Override
	public UserDTO selectKakaoMember(UserDTO userDTO) {
		return userDAO.selectKakaoMember(userDTO);
	}

	@Override
	public UserDTO searchMember(UserDTO userDTO) {
		return null;
	}

}
