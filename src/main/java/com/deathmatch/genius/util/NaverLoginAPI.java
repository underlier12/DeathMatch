package com.deathmatch.genius.util;

import org.springframework.stereotype.Component;
import com.github.scribejava.core.builder.api.DefaultApi20;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class NaverLoginAPI extends DefaultApi20 {

	protected NaverLoginAPI() {
	}

	private static class InstanceHolder {
		private static final NaverLoginAPI INSTANCE = new NaverLoginAPI();
	}

	public static NaverLoginAPI instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return "https://nid.naver.com/oauth2.0/authorize";
	}

}
