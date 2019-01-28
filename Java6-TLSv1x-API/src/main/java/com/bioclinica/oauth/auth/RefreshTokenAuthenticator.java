package com.bioclinica.oauth.auth;

import java.util.Map;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
/**
 * 
 * @author Ashish.Trivedi
 * This clas will take care to fetch Access toke from refresh token
 *
 */
public class RefreshTokenAuthenticator extends Authenticater {
	protected RefreshTokenAuthenticator() {

	}

	@Override
	public String getAccessToken() throws Exception {
		OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenUrl).setClientSecret(clientSecret)
				.setClientId(clientId).setRedirectURI(callback).setRefreshToken(refreshToken)
				.setGrantType(GrantType.REFRESH_TOKEN).setScope(scope).buildBodyMessage();
		request.addHeader("Authorization", encode(clientId + ":" + clientSecret));
		CustomHttpClient ucc = CustomHttpClient.getCustomHttpClient();
		org.apache.oltu.oauth2.client.OAuthClient oAuthClient = new org.apache.oltu.oauth2.client.OAuthClient(ucc);
		OAuthJSONAccessTokenResponse resp = oAuthClient.resource(request, OAuth.HttpMethod.POST,
				OAuthJSONAccessTokenResponse.class);
		System.out.println(resp.getAccessToken());
		return "Bearer " + resp.getAccessToken().trim();
	}

	public static void main(String[] args) throws Exception {
		OAuthClientRequest request = OAuthClientRequest.tokenLocation("https://oauth.bioclinica.com/trident-intg-proxy-api/oauth/token").setClientSecret("027670B2351d4e35b6C7CA748f3D9198")
				.setClientId("ea77b91beb784bc3b9280b7e1d98e1ed").setRedirectURI("https://www.getpostman.com/oauth2/callback").setRefreshToken("d9d04ef0be664b1cabd87f80d4f4662a")
				.setGrantType(GrantType.REFRESH_TOKEN).setScope("Admin").buildBodyMessage();
		request.addHeader("Authorization", encode("ea77b91beb784bc3b9280b7e1d98e1ed" + ":" + "027670B2351d4e35b6C7CA748f3D9198"));
		URLConnectionClient ucc = new URLConnectionClient();
		org.apache.oltu.oauth2.client.OAuthClient oAuthClient = new org.apache.oltu.oauth2.client.OAuthClient(ucc);
		OAuthJSONAccessTokenResponse resp = oAuthClient.resource(request, OAuth.HttpMethod.POST,
				OAuthJSONAccessTokenResponse.class);
		System.out.println(resp.getAccessToken());
		System.out.println("Bearer " + resp.getAccessToken().trim());
	}

}
