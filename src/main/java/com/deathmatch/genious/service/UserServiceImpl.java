package com.deathmatch.genious.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import com.deathmatch.genious.dao.UserDAO;
import com.deathmatch.genious.domain.LoginDTO;
import com.deathmatch.genious.domain.UserDTO;
import lombok.extern.log4j.Log4j;

// 서비스는 DTO 에서 얻은정보를 효율적으로 Controller에 조립하여 넘겨준다
// 서비스에서 DTO,VO를 조립한다
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
	public int changePw(UserDTO currentUser,String changePw) {
		UserDTO changePwUser = new UserDTO(currentUser.getUserEmail(),changePw);
		int result = userDAO.changePw(changePwUser);
		return result;
	}
	
	@Override
	public boolean checkPw(UserDTO currentUser,String currentPw) {
		String getPassword = userDAO.getUserPassword(currentUser);
		return getPassword.equals(currentPw);
	}
	
	@Override
	public UserDTO findId(UserDTO userDTO) {
		return null;
	}

	@Override
	public UserDTO findPw(UserDTO userDTO) {
		String userEmail = userDTO.getUserEmail();
		log.info("=== 이메일 메일  === : " + userEmail);
		Random random = new Random();
		int ranNum = random.nextInt(79999) + 10000;
		log.info("=== 랜덤 PassWord === : " + "death"+ranNum);
		String ranPassword = "death" + Integer.toString(ranNum);
		UserDTO changePwUser = new UserDTO(userEmail,ranPassword);
		userDAO.changePw(changePwUser);
		// 패스워드 변경 완료
		UserDTO user = userDAO.selectUser(changePwUser);
		return user;
	}

	/*
	 * 카카오 회원이 없으면 새로 등록, 있으면 해당 정보 값을 가져온다 
	 * cnt로 유저체크하는 것이 아닌, DB에서 유저 체크를 할 경우 새로
	 * 회원 가입시 NULL 값이 발생할 수 있다
	 */
	@Override
	public UserDTO kakaoLogin(UserDTO kakaoUser) {
		int userCnt = userDAO.countMember(kakaoUser);
		String userEmail = kakaoUser.getUserEmail();
		String userId = userEmail.substring(0,userEmail.indexOf('@'));
		log.info("현재 이메일 : " + userEmail);
		log.info("현재 아이디 : " + userId);
		if (userCnt>0) {
			kakaoUser = userDAO.selectKakaoMember(kakaoUser);
			log.info("이미 등록된 회원 입니다!");
		} else{
			kakaoUser.setUserId(userId);
			userDAO.insertKakaoMember(kakaoUser);
			log.info("Kakao 회원 가입 성공!");
		}
		return kakaoUser;
	}

	@Override
	public UserDTO selectKakaoMember(UserDTO userDTO) {
		return userDAO.selectKakaoMember(userDTO);
	}
	
	/*
	 * 네이버 회원이 없으면 새로 등록, 있으면 해당 정보 값을 가져온다 
	 * cnt로 유저체크하는 것이 아닌, DB에서 유저 체크를 할 경우 새로
	 * 회원 가입시 NULL 값이 발생할 수 있다
	 */
	@Override
	public UserDTO naverLogin(UserDTO naverUser) {
		int userCnt = userDAO.countMember(naverUser);
		String userEmail = naverUser.getUserEmail();
		String userId = userEmail.substring(0,userEmail.indexOf('@'));
		log.info("현재 아이디 : " + userEmail);
		log.info("현재 아이디 : " + userId);
		if (userCnt>0) {
			naverUser = userDAO.selectNaverMember(naverUser);
			log.info("이미 등록된 회원 입니다!");
		} else{
			naverUser.setUserId(userId);
			userDAO.insertKakaoMember(naverUser);
			log.info("Naver 회원 가입 성공!");
		}
		return naverUser;
	}

	@Override
	public UserDTO selectNaverMember(UserDTO userDTO) {
		return userDAO.selectNaverMember(userDTO);
	}

	@Override
	public int checkUserEmail(UserDTO userDTO) {
		int userCnt = userDAO.countMember(userDTO);
		log.info(userCnt);
		int result = 0;
		if(userCnt>0) {
			result = 1;
		}else {
			result =0;
		}
		return result;
	}

}
