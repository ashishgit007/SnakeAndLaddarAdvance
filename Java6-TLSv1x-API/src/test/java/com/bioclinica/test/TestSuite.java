package com.bioclinica.test;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.hornetq.utils.json.JSONObject;
import org.junit.Test;

import com.bioclinica.oauth.auth.Authenticater;
import com.bioclinica.psintg.commons.GenericConnection;
import com.bioclinica.psintg.http.client.CloudHubRestImpl;
import com.bioclinica.psintg.http.client.CustomSender;
import com.bioclinica.psintg.http.client.GenericPost;
import com.tsi9.tig.intg.muleApi.facade.IcloudHubRest;

public class TestSuite {

	@Test
	public void httpBasicTest() throws Exception {
		Map<String, String> hmm = new HashMap<String, String>();
		hmm.put("grant_flow_type", "Basic");
		hmm.put("login", "trident");
		hmm.put("password", "trident");
		Authenticater aa = Authenticater.getInstance(hmm);
		// System.out.println(aa.getAccessToken());
		assertEquals("Basic dHJpZGVudDp0cmlkZW50", aa.getAccessToken());
	}

	@Test
	public void oauthRefreshTokenTest() throws OAuthSystemException, OAuthProblemException {
		String tokenUrl = "https://oauth.bioclinica.com/trident-intg-proxy-api/oauth/token";
		String clientId = "188c116f62d447ad87973ff064d3633c";
		String clientSecret = "14a6536a50c94195b01518c75c5fdC49";
		String refreshToken = "1c749425d855461ebbb2bc88df244aae";
		String scope = "Admin";
		OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenUrl).setClientSecret(clientSecret)
				.setClientId(clientId).setRedirectURI("https://www.getpostman.com/oauth2/callback")
				.setRefreshToken(refreshToken).setGrantType(GrantType.REFRESH_TOKEN).setScope("Admin")
				.buildBodyMessage();
		request.addHeader("Authorization", encode(clientId + ":" + clientSecret));
		URLConnectionClient ucc = new URLConnectionClient();
		org.apache.oltu.oauth2.client.OAuthClient oAuthClient = new org.apache.oltu.oauth2.client.OAuthClient(ucc);
		OAuthJSONAccessTokenResponse resp = oAuthClient.resource(request, OAuth.HttpMethod.POST,
				OAuthJSONAccessTokenResponse.class);
		int length = resp.getAccessToken().split("\\.").length;
		System.out.println(length);
		assertEquals(length + "", 3 + "");
	}

	@Test
	public void customPostTest() throws Exception {
		System.out.println("#################################### customPostTest");
		// To make it consistent
		CustomSender cloudHubRest = new CloudHubRestImpl();
		JSONObject jsonResponse = cloudHubRest.customPost("testinggg", "testingstudy", getStudyConfig());
		String actuall = (String) jsonResponse.get("responseMessage");
		String expected = "success";
		assertEquals(actuall, expected);
	}

	@Test
	public void postToCloudHubRestApiTest() throws Exception {
		System.out.println("#################################### postToCloudHubRestApiTest");
		// To make it consistent
		IcloudHubRest cloudHubRest1 = new CloudHubRestImpl();
		JSONObject json1 = cloudHubRest1.postToCloudHubRestApi("testinggg", "testingstudy", getStudyConfig());
		String actuall = (String) json1.get("responseMessage");
		String expected = "success";
		assertEquals(actuall, expected);
	}

	@Test
	public void genericPostTest() throws Exception {
		System.out.println("#################################### genericPostTest");
		Map<String, String> hm = new HashMap<String, String>();
		String inputMessage = "{\r\n  \"tridentMsgType\": \"ShipResp\",\r\n  \"payload\": \"<?xml version='1.0' encoding='UTF-8'?>\\n<ns0:shipRespMsg xmlns:ns0=\\\"http://trident.bioclinica.com/intg/jaxb\\\">\\n      <crudz>U</crudz>\\n  <originator>Catalent</originator>\\n  <errorMsg></errorMsg>\\n  <shipmentId>8</shipmentId>\\n  <statusOk>true</statusOk>\\n</ns0:shipRespMsg>\",\r\n  \"correlationId\": \"d671e1d0-6f29-11e8-bcd3-6a2220524153\"\r\n}";
		CustomSender sender = new GenericPost();
		hm.put("wsURL", "https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData");
		hm.put("login", "trident");
		hm.put("password", "trident"); // hm.put("port", "443");
		hm.put("contentType", "application/json");
		JSONObject jsonResponse1 = sender.customPost(inputMessage, "tri", hm);
		String actuall = (String) jsonResponse1.get("responseMessage");
		String expected = "success";
		assertEquals(actuall, expected);
	}

	@Test
	public void customRavePostTest() throws Exception {
		System.out.println("#################################### customRavePostTest");
		CustomSender cloudHubRest2 = new CloudHubRestImpl();
		HashMap<String, String> hmh = (HashMap<String, String>) getStudyConfig();
		hmh.put("wsURL", "https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData");
		hmh.put("login", "trident");
		hmh.put("password", "trident");
		hmh.put("grant_flow_type", "Basic");
		JSONObject jsonResponse2 = cloudHubRest2.customPost("testinggg", "testingstudy", hmh);
		String actuall = (String) jsonResponse2.get("responseMessage");
		String expected = "success";
		assertEquals(actuall, expected);
	}

	@Test
	public void genericGetConnection() throws Exception {
		String inputMessage1 = "{\r\n  \"tridentMsgType\": \"ShipResp\",\r\n  \"payload\": \"<?xml version='1.0' encoding='UTF-8'?>\\n<ns0:shipRespMsg xmlns:ns0=\\\"http://trident.bioclinica.com/intg/jaxb\\\">\\n      <crudz>U</crudz>\\n  <originator>Catalent</originator>\\n  <errorMsg></errorMsg>\\n  <shipmentId>8</shipmentId>\\n  <statusOk>true</statusOk>\\n</ns0:shipRespMsg>\",\r\n  \"correlationId\": \"d671e1d0-6f29-11e8-bcd3-6a2220524153\"\r\n}";
		HttpsURLConnection con = GenericConnection.getHandShakedConnection(
				"https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData", 2000, 443);
		con.setRequestProperty("content-type", "application/xml");
		con.setRequestMethod("POST");
		String loginPassword = "trident:trident";
		String b64encoded = org.hornetq.utils.Base64.encodeBytes(loginPassword.getBytes());
		con.setRequestProperty("Authorization", "Basic " + b64encoded);
		con.setDoOutput(true);
		BufferedOutputStream buferedOutputStream = new BufferedOutputStream(con.getOutputStream(), 1024);
		buferedOutputStream.write(inputMessage1.getBytes());
		buferedOutputStream.flush();
		buferedOutputStream.write(inputMessage1.getBytes("UTF-8"));
		int responseCode = con.getResponseCode();
		System.out.println("genericGetConnection " + responseCode);
		String actuall = responseCode + "";
		String expected = "401";
		assertEquals(actuall, expected);
	}

	public String encode(String str) {
		byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
		return "Basic " + new String(encodedBytes);
	}

	public static Map<String, String> getStudyConfig() {
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("oAuthEndPoint", "https://oauth.bioclinica.com/trident-intg-proxy-api/oauth/token");
		hm.put("oAuthRefresh_token", "d9d04ef0be664b1cabd87f80d4f4662a");
		hm.put("oAuthClient_id", "ea77b91beb784bc3b9280b7e1d98e1ed");
		hm.put("oAuthClient_secret", "027670B2351d4e35b6C7CA748f3D9198");
		hm.put("Mulehost", "trident-intg-proxy-api.cloudhub.io");
		hm.put("Sslport", "443");
		hm.put("Intg-URL", "trident-trx007-rave-intg-uat.cloudhub.io");
		hm.put("HttpConnectionTimeout", "3000");
		hm.put("HttpSocketTimeout", "30000");
		hm.put("contentType", "application/xml");
		hm.put("grant_flow_type", "refresh_token");
		hm.put("wsURL", "https://trident-intg-proxy-api.cloudhub.io/api/SigningKeySubjectEvent");
		// hm.put("oAuthBasicAuthCode","=ZWE3N2I5MWJlYjc4NGJjM2I5MjgwYjdlMWQ5OGUxZWQ6MDI3NjcwQjIzNTFkNGUzNWI2QzdDQTc0OGYzRDkxOTg=");
		return hm;
	}

}
