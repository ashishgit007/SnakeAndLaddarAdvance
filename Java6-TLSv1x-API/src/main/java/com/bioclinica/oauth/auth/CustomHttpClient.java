package com.bioclinica.oauth.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.oltu.oauth2.client.HttpClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthClientResponse;
import org.apache.oltu.oauth2.client.response.OAuthClientResponseFactory;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import com.bioclinica.psintg.commons.Defaults;
/**
 * 
 * @author Ashish.Trivedi
 *Responsibility of this class is to inject Oauth Server Timeouts

 */
public class CustomHttpClient extends URLConnectionClient implements HttpClient {
    public int readTimeout;
    public int connectTimeOut;
	// add Timeouts
    public static  CustomHttpClient getCustomHttpClient()
    {
    	CustomHttpClient instance = new CustomHttpClient();
    	instance.readTimeout=Defaults.OAUTH_READTIMEOUT_MS_DEFAULT;
    	instance.connectTimeOut=Defaults.OAUTH_CONNECTION_TIMEOUT_MS_DEFAULT;
    	return instance;    	
    	
    }
    
    private CustomHttpClient()
    {}

	public <T extends OAuthClientResponse> T execute(OAuthClientRequest request, Map<String, String> headers,
			String requestMethod, Class<T> responseClass) throws OAuthSystemException, OAuthProblemException {
		
		System.out.println("Custom OAUTH implementor");
		String responseBody = null;
		URLConnection c = null;
		int responseCode = 0;
		try {
			URL url = new URL(request.getLocationUri());

			c = url.openConnection();
			responseCode = -1;
			if ((c instanceof HttpURLConnection)) {
				HttpURLConnection httpURLConnection = (HttpURLConnection) c;
				if ((headers != null) && (!headers.isEmpty())) {
					for (Map.Entry<String, String> header : headers.entrySet()) {
						httpURLConnection.addRequestProperty((String) header.getKey(), (String) header.getValue());
					}
				}
				if (request.getHeaders() != null) {
					for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
						httpURLConnection.addRequestProperty((String) header.getKey(), (String) header.getValue());
					}
				}
				if (!OAuthUtils.isEmpty(requestMethod)) {
					httpURLConnection.setRequestMethod(requestMethod);
					if (requestMethod.equals("POST")) {
						httpURLConnection.setDoOutput(true);
						OutputStream ost = httpURLConnection.getOutputStream();
						PrintWriter pw = new PrintWriter(ost);
						pw.print(request.getBody());
						pw.flush();
						pw.close();
					}
				} else {
					httpURLConnection.setRequestMethod("GET");
				}
				httpURLConnection.setReadTimeout(this.readTimeout);
				httpURLConnection.setConnectTimeout(this.connectTimeOut);
				httpURLConnection.connect();

				responseCode = httpURLConnection.getResponseCode();
				InputStream inputStream;
				if ((responseCode == 400) || (responseCode == 401)) {
					inputStream = httpURLConnection.getErrorStream();
				} else {
					inputStream = httpURLConnection.getInputStream();
				}
				responseBody = OAuthUtils.saveStreamAsString(inputStream);
			}
		} catch (IOException e) {
			throw new OAuthSystemException(e);
		}
		return OAuthClientResponseFactory.createCustomResponse(responseBody, c.getContentType(), responseCode,
				responseClass);
	}

}
