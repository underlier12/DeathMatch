package com.deathmatch.genius.controller;

import java.io.IOException;

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

import com.deathmatch.genius.domain.LoginDTO;
import com.deathmatch.genius.domain.UserDTO;
import com.deathmatch.genius.service.UserService;
import com.deathmatch.genius.util.Email;
import com.deathmatch.genius.util.EmailSender;
import com.deathmatch.genius.util.KakaoConnectionUtil;
import com.deathmatch.genius.util.NaverLoginBO;
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

	public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService,
			NaverLoginBO naverLoginService, EmailSender emailSender) {
		this.kakaoLoginService = kakaoLoginService;
		this.userService = userService;
		this.naverLoginService = naverLoginService;
		this.emailSender = emailSender;
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
	
	@GetMapping("/login")
	public void loginGet(@ModelAttribute("loginDTO") LoginDTO loginDTO) {

	}
	
	@ResponseBody
	@PostMapping("/join")
	public ResponseEntity<String> joinMember(@RequestBody UserDTO userDTO) {
		userService.insertMember(userDTO);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}

	// 비밀번호 변경
	@PostMapping("/changePw")
	public String changePw(@RequestParam String currentPw, @RequestParam String changePw, HttpSession session,
			Model model) {
		UserDTO currentUser = (UserDTO)session.getAttribute("login");
		if(userService.checkPw(currentUser, currentPw)) {
			userService.changePw(currentUser,changePw);
		}else {
			model.addAttribute("msg","비밀번호가 일치하지 않습니다");
			return "/user/changePw";
		}
		return "/user/myPage";
	}

	// 로그 아웃
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Object currentUser = session.getAttribute("login");
		if (currentUser != null) {
			session.removeAttribute("login");
			session.invalidate();
		}
		return "redirect:/";
	}
	
	// 네이버 토큰 얻어오기
	@GetMapping("/naverGetToken")
	public void naverGetToken(HttpSession session, Model model) {
		String naverAuthUrl = naverLoginService.getAuthorizationUrl(session);
		log.info("naver :" + naverAuthUrl);
		model.addAttribute("url", naverAuthUrl);
	}

	// local 유저 로그인 확인하기
	@PostMapping("/loginPost")
	public String loginPost(LoginDTO loginDTO, HttpSession session, Model model) {
		UserDTO localUserDTO = userService.login(loginDTO);
		log.info("loginPost");
		if (localUserDTO == null) {
			log.info("Login User is Empty");
			model.addAttribute("msg", "등록되지 않은 회원이거나,아이디 비밀번호가 일치하지 않습니다");
			return "user/loginHome";
		} else {
			log.info("Local User :" + localUserDTO.toString());
			model.addAttribute("userDTO", localUserDTO);
			return "user/loginPost";
		}
	}

	// kakaoUser 로그인
	@GetMapping("/kakaoLogin")
	public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
		String accessToken = kakaoLoginService.getAccessToken(code);
		UserDTO kakaoUser = kakaoLoginService.getUserInfo(accessToken);
		log.info("login Info: " + kakaoUser.toString());
		// 유저 정보가 없으면 새로운 vo, 있으면 기존의 정보를 불러온다.
		userService.kakaoLogin(kakaoUser);
		model.addAttribute("userDTO", kakaoUser);
		return "main/gameHome";
	}

	// NaverUser 로그인
	@RequestMapping(value = "/naverLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String naverLogin(Model model, @RequestParam String code, @RequestParam String state, HttpSession session)
			throws IOException, ParseException {
		OAuth2AccessToken oauthToken = naverLoginService.getAccessToken(session, code, state);
		//String apiResult = naverLoginService.getUserProfile(oauthToken);
		UserDTO naverUser = naverLoginService.getUserInfo(naverLoginService.getUserProfile(oauthToken));
		userService.naverLogin(naverUser);
		log.info("NaverUser :" + naverUser.toString());
		model.addAttribute("userDTO", naverUser);
		return "main/gameHome";
	}

	// 이메일 확인하기
	@ResponseBody
	@PostMapping("/checkEmail")
	public ResponseEntity<Integer> checkMember(@RequestBody UserDTO userDTO) {
		return new ResponseEntity<>(userService.checkUserEmail(userDTO),HttpStatus.OK);
	}

	// 비밀번호 찾기
	@ResponseBody
	@PostMapping("/findPw")
	public ResponseEntity<String> findPw(@RequestBody UserDTO userDTO) {
		UserDTO findPwUser = userService.findPw(userDTO); // 새로운 Pw를 생성한 User
		Email email = userService.sendEmail(findPwUser);
		try {
			emailSender.SendEmail(email);
			return new ResponseEntity<>("Success",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
		}
	}

}
