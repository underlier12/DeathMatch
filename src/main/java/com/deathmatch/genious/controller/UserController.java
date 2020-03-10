package com.deathmatch.genious.controller;

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
import com.deathmatch.genious.domain.LoginDTO;
import com.deathmatch.genious.domain.UserDTO;
import com.deathmatch.genious.service.UserService;
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

	public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService,
			NaverLoginBO naverLoginService) {
		this.kakaoLoginService = kakaoLoginService;
		this.userService = userService;
		this.naverLoginService = naverLoginService;
	}

	@GetMapping("/loginHome")
	public String loginHome(HttpSession session, Model model, HttpServletRequest request) {
		String naverAuthUrl = naverLoginService.getAuthorizationUrl(session);
		log.info("naver :" + naverAuthUrl);
		model.addAttribute("url", naverAuthUrl);
		return "/user/loginHome";
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
		}else {
			log.info("Local User :" + LocalUserDTO.toString());
			model.addAttribute("userDTO",LocalUserDTO); 
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
	public String naverLogin(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException, ParseException {

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

	@PostMapping("/join")
	public @ResponseBody ResponseEntity<String> joinMember(@RequestBody UserDTO userDTO) {
		ResponseEntity<String> entity = null;
		try {
			userService.insertMember(userDTO);
			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@PostMapping("/checkEmail")
	public @ResponseBody ResponseEntity<String> checkMember(@RequestBody String userEmail){
		ResponseEntity<String> entity = null;
		
		
		return entity;
	}

}
