package com.bioclinica.oauth.auth;

import java.util.Map;

/**
 * 
 * @author Ashish.Trivedi This uses Basic Auth
 */
public class HttpBasicAuthenticator extends Authenticater {

	protected HttpBasicAuthenticator() {

	}

	@Override
	public String getAccessToken() throws Exception {
		String loginPassword = login + ":" + password;
		return  encode(loginPassword);
	}
}
