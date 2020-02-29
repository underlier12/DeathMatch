package com.deathmatch.genious.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService,NaverLoginBO naverLoginService) {
        this.kakaoLoginService = kakaoLoginService;
        this.userService = userService;
        this.naverLoginService = naverLoginService;
    }
    
    @GetMapping("/loginHome")
    public String loginHome(HttpSession session,Model model){
        String naverAuthUrl = naverLoginService.getAuthorizationUrl(session);
        log.info("naver :" +naverAuthUrl);
        model.addAttribute("url",naverAuthUrl);
        return "/user/loginHome";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response ,HttpSession session){
        Object currentObj = session.getAttribute("login");
        if(currentObj != null){
            UserDTO userDTO = (UserDTO)currentObj;
            session.removeAttribute("login");
            session.invalidate();
        }
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public void loginGet(@ModelAttribute("loginDTO")LoginDTO loginDTO){
    	
    }
    
    @PostMapping("/loginPost")
    public String loginPost(LoginDTO loginDTO , HttpSession session, Model model){
        UserDTO userDTO = userService.login(loginDTO);
        if(userDTO == null){
            log.info("Login User is Empty");
            model.addAttribute("msg","등록되지 않은 회원입니다");
            return "/user/loginHome";
        }else {
        	model.addAttribute("userDTO",userDTO);
			return "gameHome" ;
        }
    }
    
    @GetMapping("/kakaoLogin")
    public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
        String accessToken = kakaoLoginService.getAccessToken(code);
        UserDTO kakaoUser = kakaoLoginService.getUserInfo(accessToken);
        log.info("AccessToken: " + accessToken);
        log.info("login Info: " + kakaoUser.toString());
        
        // 유저 정보가 없으면 새로운 vo, 있으면 기존의 정보를 불러온다.
        userService.kakaoLogin(kakaoUser);
        model.addAttribute("userDTO", kakaoUser);
        return "gameHome";
    }
    
    @RequestMapping(value ="/naverLogin", method ={RequestMethod.GET,RequestMethod.POST})
    public String naverLogin(Model model, @RequestParam String code,@RequestParam String state,HttpSession session) throws IOException, ParseException {
        OAuth2AccessToken oauthToken = naverLoginService.getAccessToken(session, code, state);
        
        String apiResult = naverLoginService.getUserProfile(oauthToken); 
        UserDTO naverUser = naverLoginService.getUserInfo(apiResult);
        userService.naverLogin(naverUser);
        log.info(naverUser.toString());
        
        model.addAttribute("userDTO",naverUser);
		/* model.addAttribute("result", apiResult); */
	
        return "gameHome";
    }
    
    @GetMapping("/join")
    public String join(){
        return "user/join";
    }

    @PostMapping("/join")
    public String joinMember(UserDTO userDTO){
        userService.insertMember(userDTO);
        return "gameHome";
    }

	
}
