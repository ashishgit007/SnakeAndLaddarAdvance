package com.bioclinica.test;

import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.codec.binary.Base64;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONObject;

import com.bioclinica.psintg.http.client.CustomSender;
import com.bioclinica.psintg.http.client.GenericPost;
import com.tsi9.tig.intg.muleApi.facade.IcloudHubRest;
import com.bioclinica.psintg.commons.Constants;
import com.bioclinica.psintg.commons.Defaults;
import com.bioclinica.psintg.commons.GenericConnection;
import com.bioclinica.psintg.http.client.CloudHubRestImpl;

/**
 * 
 * @author Ashish.Trivedi Thsis class is just to test this code, when not
 *         deployed in server
 *
 */
public class MainTestDriverMule {

	public static void main(String[] args) throws Exception {

		System.out.println("#################################### NEW ###### CLOUD HUB");
		// To make it consistent
		CustomSender cloudHubRest = new CloudHubRestImpl();
		JSONObject jsonResponse = cloudHubRest.customPost("testinggg", "testingstudy", getStudyConfig());
		System.out.println("--------------------------------------------- customPost");
		System.out.println(jsonResponse);
		System.out.println("#################################### BACK WARD");
		// For backward Old Code
		IcloudHubRest cloudHubRest1 = new CloudHubRestImpl();
		JSONObject json1 = cloudHubRest1.postToCloudHubRestApi("testinggg", "testingstudy", getStudyConfig());
		System.out.println("--------------------------------------------- postToCloudHubRestApi");
		System.out.println(json1);
		// for RAVE
		System.out.println("#################################### RAVE GENERIC #############");

		Map<String, String> hm = new HashMap<String, String>();
		String inputMessage = "{\r\n  \"tridentMsgType\": \"ShipResp\",\r\n  \"payload\": \"<?xml version='1.0' encoding='UTF-8'?>\\n<ns0:shipRespMsg xmlns:ns0=\\\"http://trident.bioclinica.com/intg/jaxb\\\">\\n      <crudz>U</crudz>\\n  <originator>Catalent</originator>\\n  <errorMsg></errorMsg>\\n  <shipmentId>8</shipmentId>\\n  <statusOk>true</statusOk>\\n</ns0:shipRespMsg>\",\r\n  \"correlationId\": \"d671e1d0-6f29-11e8-bcd3-6a2220524153\"\r\n}";
		CustomSender sender = new GenericPost();
		hm.put("wsURL", "https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData");
		hm.put("login", "trident");
		hm.put("password", "trident"); // hm.put("port", "443");
		hm.put("contentType", "application/json");
		JSONObject jsonResponse1 = sender.customPost(inputMessage, "tri", hm);
		System.out.println("Server Response is " + jsonResponse1.toString());

		System.out.println("#################################### RAVE INJECTOR HTTP############");
		CustomSender cloudHubRest2 = new CloudHubRestImpl();
		HashMap<String, String> hmh = (HashMap<String, String>) getStudyConfig();
		hmh.put("wsURL", "https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData");
		hmh.put("login", "trident");
		hmh.put("password", "trident");
		hmh.put("grant_flow_type", "Basic");

		JSONObject jsonResponse2 = cloudHubRest2.customPost("testinggg", "testingstudy", hmh);
		System.out.println("--------------------------------------------- customPost");
		System.out.println(jsonResponse2);

		System.out.println("#############TESTING GENERIC STUFFFF#################");

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
		System.out.println(responseCode);

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
