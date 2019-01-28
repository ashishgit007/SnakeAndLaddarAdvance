package com.bioclinica.oauth.auth;

import java.util.HashMap;
import java.util.Map;
/**
 * Common Abstraction for Various Authenticator 
 * Oauth PASSWORD(RO) credentials and 
 * 
 */


public abstract class Authenticater {
	String tokenUrl;
	String clientId;
	String clientSecret;
	String refreshToken;
	String scope;
	String callback;
	String login;
	String password;
	String Scope;

	public abstract String getAccessToken() throws Exception;

	private static final Map<String, String> ioc = new HashMap<String, String>();

	static {
		ioc.put("refresh_token", "com.bioclinica.oauth.auth.RefreshTokenAuthenticator");
		ioc.put("client_credentials", "com.bioclinica.oauth.auth.ClientCredential");
		ioc.put("Basic", "com.bioclinica.oauth.auth.HttpBasicAuthenticator");
	}

	public static String  encode(String str) {
		byte[] encodedBytes = org.bouncycastle.util.encoders.Base64.encode(str.getBytes());
		return "Basic " + new String(encodedBytes);
	}

	public static Authenticater getInstance( Map<String, String> studyConfig)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String type = studyConfig.get("grant_flow_type");
		String authImpl = ioc.get(type);
		Authenticater authenticator = (Authenticater) Class.forName(authImpl).newInstance();
		authenticator.init(studyConfig);
		return authenticator;

	}

	private void init(Map<String, String> studyConfig) {
		refreshToken = studyConfig.get("oAuthRefresh_token");
		clientId = studyConfig.get("oAuthClient_id");
		clientSecret = studyConfig.get("oAuthClient_secret");
		tokenUrl = studyConfig.get("oAuthEndPoint");
		scope = studyConfig.get("scope") == null ? "Admin" : studyConfig.get("scope");
		callback = studyConfig.get("callback") == null ? "https://www.getpostman.com/oauth2/callback" : studyConfig.get("callback");
		login = studyConfig.get("login");
		password = studyConfig.get("password");		

	}

}
