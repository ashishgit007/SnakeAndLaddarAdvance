package com.bioclinica.oauth.auth;

import java.util.Map;


import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
/**
 * 
 * @author Ashish.Trivedi
 *
 */
public class ClientCredential extends Authenticater {
protected ClientCredential() {
		
	}
	@Override
	public String getAccessToken() throws Exception {
		encode(clientId + ":" + clientSecret);
		OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenUrl).setClientSecret(clientSecret)
				.setClientId(clientId).setRedirectURI(callback)
				.setGrantType(GrantType.CLIENT_CREDENTIALS).setScope(scope).buildBodyMessage();
		request.addHeader("Authorization", encode(clientId + ":" + clientSecret));		
		URLConnectionClient ucc = new URLConnectionClient();
		org.apache.oltu.oauth2.client.OAuthClient oAuthClient = new org.apache.oltu.oauth2.client.OAuthClient(ucc);
		OAuthJSONAccessTokenResponse resp = oAuthClient.resource(request, OAuth.HttpMethod.POST,
				OAuthJSONAccessTokenResponse.class);
		System.out.println(resp.getAccessToken());
		return "Bearer " + resp.getAccessToken().trim();
		
		
	}

}
