package com.deathmatch.genious.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.deathmatch.genious.domain.UserDTO;

import lombok.extern.log4j.Log4j;

@Log4j
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		log.info("beforeHandshake");
		
		ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
		HttpServletRequest req = ssreq.getServletRequest();
		
		String id = (String)req.getSession().getAttribute("id");
//		attributes.put("userId", id);
		log.info("session id : " + id);
		
		log.info("URI : " + request.getURI());
		
		String id2 = req.getSession().getId();
		log.info("session id2 : " + id2);
		
		UserDTO userDTO = (UserDTO) req.getSession().getAttribute("login");
		String userEmail = userDTO.getUserEmail();
		log.info("userDTO.getEmail : " + userEmail);
		
		attributes.put("userEmail", userEmail);
		
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		log.info("afterHandshake");
		
		ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
		HttpServletRequest req = ssreq.getServletRequest();
		
		String id = (String)req.getSession().getAttribute("id");
		log.info("session id : " + id);
		
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
