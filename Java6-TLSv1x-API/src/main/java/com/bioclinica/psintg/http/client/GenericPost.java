package com.bioclinica.psintg.http.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;
import org.hornetq.utils.Base64;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;

import com.bioclinica.psintg.commons.Constants;
import com.bioclinica.psintg.commons.Defaults;

/**
 * 
 * @author Ashish.Trivedi This class is coded to Send ODM to rave.
 * 
 *         ConnectTimeout :- Sets a specified timeout value, in milliseconds, to
 *         be used when opening a communications link to the resource referenced
 *         by this URLConnection. If the timeout expires before the connection
 *         can be established, a java.net.SocketTimeoutException is raised. A
 *         timeout of zero is interpreted as an infinite timeout.
 *
 *
 *         ReadTimeout :- Sets the read timeout to a specified timeout, in
 *         milliseconds. A non-zero value specifies the timeout when reading
 *         from Input stream when a connection is established to a resource. If
 *         the timeout expires before there is data available for read, a
 *         java.net.SocketTimeoutException is raised. A timeout of zero is
 *         interpreted as an infinite timeout.
 *
 *         Usage: 
 *         
		 Map<String, String> hm = new HashMap<String, String>(); String
		 inputMessage =
		 "{\r\n  \"tridentMsgType\": \"ShipResp\",\r\n  \"payload\": \"<?xml version='1.0' encoding='UTF-8'?>\\n<ns0:shipRespMsg xmlns:ns0=\\\"http://trident.bioclinica.com/intg/jaxb\\\">\\n      <crudz>U</crudz>\\n  <originator>Catalent</originator>\\n  <errorMsg></errorMsg>\\n  <shipmentId>8</shipmentId>\\n  <statusOk>true</statusOk>\\n</ns0:shipRespMsg>\",\r\n  \"correlationId\": \"d671e1d0-6f29-11e8-bcd3-6a2220524153\"\r\n}"; 
		 CustomSender sender = new GenericPost(); 
		 hm.put("wsURL", "https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData"); 
		 hm.put("login", "trident");
		 hm.put("password", "trident"); //hm.put("port", "443");
		 hm.put("contentType", "application/json"); 		
		 JSONObject jsonResponse = new GenericPost().customPost(inputMessage, "tri", hm); System.out.println("Server Response is " +
		 jsonResponse.toString());
		 
 */

public class GenericPost extends CustomSender {

	int responseCode = 0;
	int outBuffer = 32 * 1024;
	public static Logger log = Logger.getLogger("GenericPost");
	Properties props = new Properties();

	@Override
	public org.hornetq.utils.json.JSONObject customPost(String payload, String studyCode, Map<String, String> config)
			throws Exception {
		String response = doGenericPost(payload, config);
		org.hornetq.utils.json.JSONObject json = convertToJson(response);
		return json;

	}

	public JSONObject convertToJson(String input) throws JSONException {
		HashMap<String, String> resp = new HashMap<String, String>();
		resp.put(Constants.WS_RESPONSE_KEY1, this.responseCode + "");
		resp.put(Constants.WS_RESPONSE_KEY2, "success");
		resp.put(Constants.WS_RESPONSE_KEY3, input);
		return new JSONObject(resp);
	}

	private String doGenericPost(String payload, Map<String, String> _config) throws MalformedURLException {
		props.putAll(_config);
		long start = System.currentTimeMillis();
		StringBuffer response = null;
		DataOutputStream wr = null;
		BufferedReader in = null;
		HttpsURLConnection con = null;
		String loginPassword = props.getProperty("login") + ":" + props.getProperty("password");
		String b64encoded = Base64.encodeBytes(loginPassword.getBytes());
		String webserviceURL = props.getProperty("wsURL");
		URL obj = new URL(webserviceURL);
		System.out.println("Rave Host is "+obj.getHost());
		try {
			con = (HttpsURLConnection) obj.openConnection();
			CustomTLSSocketFactory tls = new CustomTLSSocketFactory(
					Integer.parseInt(props.getProperty(Constants.WS_READTIMEOUT_MS_1,
							props.getProperty(Constants.WS_READTIMEOUT_MS_2, Defaults.WS_READTIMEOUT_MS_DEFAULT))));
			tls.setHost(obj.getHost());
			tls.setPort(Integer.parseInt(props.getProperty(Constants.WS_HOST_PORT, Defaults.SSLPORT)));
			con.setSSLSocketFactory(tls);
			con.setConnectTimeout(Integer.parseInt(props.getProperty(Constants.WS_CONNECTION_TIMEOUT_MS_1, props
					.getProperty(Constants.WS_CONNECTION_TIMEOUT_MS_2, Defaults.WS_CONNECTION_TIMEOUT_MS_DEFAULT))));
			con.setReadTimeout(Integer.parseInt(props.getProperty(Constants.WS_READTIMEOUT_MS_1,
					props.getProperty(Constants.WS_READTIMEOUT_MS_2, Defaults.WS_READTIMEOUT_MS_DEFAULT))));
			con.setRequestProperty("content-type", props.getProperty(Constants.WS_CONTENT_TYPE, Defaults.CONTENTTYPE));
			con.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			con.setRequestMethod("POST");
			con.setRequestProperty("Host", obj.getHost());
			con.setRequestProperty("Authorization", "Basic " + b64encoded);
			con.setDoOutput(true);
			BufferedOutputStream buferedOutputStream = new BufferedOutputStream(con.getOutputStream(), outBuffer);
			buferedOutputStream.write(payload.getBytes());
			buferedOutputStream.flush();
			buferedOutputStream.write(payload.getBytes("UTF-8"));
			responseCode = con.getResponseCode();
			System.out.println("Response Code : " + responseCode);
			if (responseCode == 200)
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			else
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine.trim());
			}
			log.info("URL Response is " + response);
			long end = System.currentTimeMillis();
			log.info("Message Succesfully Posted , Time taken" + ((end - start) / 60000) + "minutes");

		} catch (Exception ex) {
			if (ex.getMessage().equalsIgnoreCase("Internal TLS error, this could be an attack")) {
				log.error("Probable timeout issue with call to the URL - " + webserviceURL, ex);
			}
			ex.printStackTrace();

			return "Error While Posting to Rave";

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}

			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		return response.toString();
	}

	

	// below code is just to unit test the code
	public static void main(String[] args) throws Exception {
		 Map<String, String> hm = new HashMap<String, String>(); String
		 inputMessage =
		 "{\r\n  \"tridentMsgType\": \"ShipResp\",\r\n  \"payload\": \"<?xml version='1.0' encoding='UTF-8'?>\\n<ns0:shipRespMsg xmlns:ns0=\\\"http://trident.bioclinica.com/intg/jaxb\\\">\\n      <crudz>U</crudz>\\n  <originator>Catalent</originator>\\n  <errorMsg></errorMsg>\\n  <shipmentId>8</shipmentId>\\n  <statusOk>true</statusOk>\\n</ns0:shipRespMsg>\",\r\n  \"correlationId\": \"d671e1d0-6f29-11e8-bcd3-6a2220524153\"\r\n}"; 
		 CustomSender sender = new GenericPost(); 
		 hm.put("wsURL", "https://origin-abbott.mdsol.com/RaveWebServices/WebService.aspx?PostODMClinicalData"); 
		 hm.put("login", "trident");
		 hm.put("password", "trident"); //hm.put("port", "443");
		 hm.put("contentType", "application/json"); 		
		 JSONObject jsonResponse = new GenericPost().customPost(inputMessage, "tri", hm); System.out.println("Server Response is " +
		 jsonResponse.toString());
		}

}
