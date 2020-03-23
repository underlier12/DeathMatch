package com.deathmatch.genious.controller;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deathmatch.genious.domain.LoginDTO;
import com.deathmatch.genious.domain.UserDTO;
import com.deathmatch.genious.service.UserService;
import com.deathmatch.genious.util.Email;
import com.deathmatch.genious.util.EmailSender;
import com.deathmatch.genious.util.KakaoConnectionUtil;
import com.deathmatch.genious.util.NaverLoginBO;
import com.github.scribejava.core.model.OAuth2AccessToken;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/user")
public class UserController {

	private final KakaoConnectionUtil kakaoLoginService;
	private final UserService userService;
	private final NaverLoginBO naverLoginService;
	private final EmailSender emailSender;
	private final Email email;

	public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService,
			NaverLoginBO naverLoginService, EmailSender emailSender, Email email) {
		this.kakaoLoginService = kakaoLoginService;
		this.userService = userService;
		this.naverLoginService = naverLoginService;
		this.emailSender = emailSender;
		this.email = email;
	}

	@GetMapping("/loginHome")
	public String loginHome(HttpSession session, Model model, HttpServletRequest request) {
		String naverAuthUrl = naverLoginService.getAuthorizationUrl(session);
		log.info("naver :" + naverAuthUrl);
		model.addAttribute("url", naverAuthUrl);
		return "/user/loginHome";
	}

	@GetMapping("/myPage")
	public String myPage() {
		return "/user/myPage";
	}

	@GetMapping("/changePw")
	public String changePwGet() {
		return "/user/changePw";
	}
	
	@GetMapping("/modifyInfo")
	public String modifyInfo() {
		return "/user/modifyInfo";
	}

	// 비밀번호 변경
	@PostMapping("/changePw")
	public String changePw(@RequestParam String currentPw, @RequestParam String changePw, HttpSession session,
			Model model) {
		Object currentSessionUser = session.getAttribute("login"); // 세션 유저는 비밀번호를 들고 다니지 않음
		UserDTO currentUser = (UserDTO)currentSessionUser;
		if(userService.checkPw(currentUser, currentPw)) {
			userService.changePw(currentUser,changePw);
		}else {
			model.addAttribute("msg","비밀번호가 일치하지 않습니다");
			return "/user/changePw";
		}
		return "/user/myPage";
	}

	@GetMapping("/login")
	public void loginGet(@ModelAttribute("loginDTO") LoginDTO loginDTO) {

	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Object currentUser = session.getAttribute("login");
		if (currentUser != null) {
			session.removeAttribute("login");
			session.invalidate();
		}
		return "redirect:/";
	}

	@PostMapping("/loginPost")
	public String loginPost(LoginDTO loginDTO, HttpSession session, Model model) {
		UserDTO LocalUserDTO = userService.login(loginDTO);
		if (LocalUserDTO == null) {
			log.info("Login User is Empty");
			model.addAttribute("msg", "등록되지 않은 회원입니다");
			return "/user/loginHome";
		} else {
			log.info("Local User :" + LocalUserDTO.toString());
			model.addAttribute("userDTO", LocalUserDTO);
			return "gameHome";
		}
	}

	@GetMapping("/kakaoLogin")
	public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
		String accessToken = kakaoLoginService.getAccessToken(code);
		UserDTO kakaoUser = kakaoLoginService.getUserInfo(accessToken);
		log.info("login Info: " + kakaoUser.toString());

		// 유저 정보가 없으면 새로운 vo, 있으면 기존의 정보를 불러온다.
		userService.kakaoLogin(kakaoUser);
		model.addAttribute("userDTO", kakaoUser);

		return "gameHome";
	}

	@RequestMapping(value = "/naverLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String naverLogin(Model model, @RequestParam String code, @RequestParam String state, HttpSession session)
			throws IOException, ParseException {

		OAuth2AccessToken oauthToken = naverLoginService.getAccessToken(session, code, state);
		String apiResult = naverLoginService.getUserProfile(oauthToken);
		UserDTO naverUser = naverLoginService.getUserInfo(apiResult);
		userService.naverLogin(naverUser);
		log.info("NaverUser :" + naverUser.toString());
		model.addAttribute("userDTO", naverUser);

		return "gameHome";
	}

	@GetMapping("/naverGetToken")
	public void naverGetToken(HttpSession session, Model model) {
		String naverAuthUrl = naverLoginService.getAuthorizationUrl(session);
		log.info("naver :" + naverAuthUrl);
		model.addAttribute("url", naverAuthUrl);
	}

	@ResponseBody
	@PostMapping("/join")
	public ResponseEntity<String> joinMember(@RequestBody UserDTO userDTO) {
		ResponseEntity<String> entity = null;
		try {
			String userEmail = userDTO.getUserEmail();
			String userId = userEmail.substring(0, userEmail.indexOf('@'));
			log.info("userEmail:" + userEmail);
			log.info("ID:" + userId);
			userDTO.setUserId(userId);
			userService.insertMember(userDTO);
			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

	@ResponseBody
	@PostMapping("/checkEmail")
	public int checkMember(@RequestBody UserDTO userDTO) {
		int cnt = 0;
		log.info(userDTO);
		cnt = userService.checkUserEmail(userDTO);
		log.info("result :" + cnt);
		return cnt;
	}

	@ResponseBody
	@PostMapping("/findPw")
	public String findPw(@RequestBody UserDTO userDTO) {
		UserDTO findPwUser = userService.findPw(userDTO);
		try {
			email.setContent("임시 비밀 번호는 " + findPwUser.getPw() + " 입니다 ");
			email.setReceiver(findPwUser.getUserEmail());
			email.setSubject("안녕하세요 Death Match 입니다." + findPwUser.getName() +" 님 임시 비밀번호를 확인해주세요" );
			log.info(email);
			emailSender.SendEmail(email);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "/user/loginHome";
	}

}
