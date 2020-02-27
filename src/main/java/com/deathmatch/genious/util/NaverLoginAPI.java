package com.deathmatch.genious.util;

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
