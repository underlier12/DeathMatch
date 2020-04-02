package com.deathmatch.genius.interceptor;

import lombok.extern.log4j.Log4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.deathmatch.genius.domain.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private static final String LOGIN = "login";
	
	/* 
	 * postHandle = Session에 컨트롤러에서 저장한 user를 저장하고 /로 리다이렉트한다.
	 * postHandle = Controller 실행후에 실행된다
	 * preHandle = 기존의 로그인 정보가 있을 경우 초기화하는 역할
	 * preHandle =  Controller 실행전에 실행된다
	 */

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();

        ModelMap modelMap = modelAndView.getModelMap();
        Object userDTO = modelMap.get("userDTO");
        log.info("Post Handle");

        if(userDTO != null){
            log.info("Login Success!");
            session.setAttribute("login",userDTO);
			UserDTO currentDTO = (UserDTO)session.getAttribute("login"); 
			String  sessionUser = currentDTO.getUserEmail();
			log.info("currentUser: " + sessionUser ); 
            response.sendRedirect("/gameHome");
        }
    }

    // 기존에 정보가 있는 경우에 삭제한다
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        log.info("preHandle");
        if(session.getAttribute("login")!=null){
            session.removeAttribute("login");
            log.info("LOGOUT");
        }
        return true;
    }
	
}
