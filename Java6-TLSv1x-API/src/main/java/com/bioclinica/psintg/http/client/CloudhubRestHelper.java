package com.bioclinica.psintg.http.client;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hornetq.utils.json.JSONObject;
import com.bioclinica.oauth.auth.Authenticater;
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
 *         HttpSocketTimeout :- Sets the read timeout to a specified timeout, in
 *         milliseconds. A non-zero value specifies the timeout when reading
 *         from Input stream when a connection is established to a resource. If
 *         the timeout expires before there is data available for read, a
 *         java.net.SocketTimeoutException is raised. A timeout of zero is
 *         interpreted as an infinite timeout.
 * 
 */
public class CloudhubRestHelper {
	protected static Log log = LogFactory.getLog(CloudhubRestHelper.class.getName());	
	String accessToken;		
	String inputMessage;	
	Properties props = new Properties();
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getInputMessage() {
		return inputMessage;
	}

	public void setInputMessage(String inputMessage) {
		this.inputMessage = inputMessage;
	}
	boolean jsonObjectResponse = true;
	Authenticater authenticater;

	public CloudhubRestHelper(String host, String restEndpoint, int port, Map<String, String> studyConfig)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException {
		super();
		props.putAll(studyConfig);
		authenticater = Authenticater.getInstance(studyConfig);
		log.info("Authenticator " + authenticater.getClass().getName() + " Injected");
	}

	public JSONObject sendPost() throws Exception {
		StringBuffer response = new StringBuffer("");
		int responseCode = 0;
		BufferedOutputStream wr = null;
		BufferedReader in = null;
		HttpsURLConnection con = null;
		accessToken = getOauthToken();
		if (null != accessToken && !accessToken.isEmpty()) {
			URL url = new URL(props.getProperty(Constants.WS_URL, Defaults.WS_URL));
			try {
				con = (HttpsURLConnection) url.openConnection();
				CustomTLSSocketFactory tls = new CustomTLSSocketFactory(Integer.parseInt(props.getProperty(Constants.WS_READTIMEOUT_MS_1, props.getProperty(Constants.WS_READTIMEOUT_MS_2, Defaults.WS_READTIMEOUT_MS_DEFAULT))));
				tls.setHost(url.getHost());
				tls.setPort(Integer.parseInt(props.getProperty(Constants.WS_HOST_PORT, Defaults.SSLPORT)));
				con.setSSLSocketFactory(tls);
				con.setConnectTimeout(Integer.parseInt(props.getProperty(Constants.WS_CONNECTION_TIMEOUT_MS_1, props.getProperty(Constants.WS_CONNECTION_TIMEOUT_MS_2, Defaults.WS_CONNECTION_TIMEOUT_MS_DEFAULT))));
				con.setReadTimeout(Integer.parseInt(props.getProperty(Constants.WS_READTIMEOUT_MS_1, props.getProperty(Constants.WS_READTIMEOUT_MS_2, Defaults.WS_READTIMEOUT_MS_DEFAULT))));
				con.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				log.debug("-------Intg-URL ---------" + props.getProperty(Constants.INTG_URL));
				log.debug("-------Mulehost ---------" + url.getHost());
				log.debug("-------accessToken ---------" + accessToken);
				log.debug("-------oAuthClient_id ---------" + props.getProperty(Constants.OAUTHSERVER_CLIENT_ID,Defaults.OAUTHCLIENT_ID));
				log.debug("-------oAuthClient_secret ---------" + props.getProperty(Constants.OAUTHSERVER_CLIENT_SECRET,Defaults.OAUTHCLIENT_SECRET));
				log.debug("-------contentType ---------" + props.getProperty(Constants.WS_CONTENT_TYPE,Defaults.CONTENTTYPE));
				
				if("Basic".equals(props.get("grant_flow_type")))
				{
					con.setRequestMethod("POST");
					con.setRequestProperty("Host", url.getHost());
					con.setRequestProperty("Authorization", accessToken);					
				}
				else
				{
				con.setRequestMethod(Constants.WS_VERB);
				con.setRequestProperty(Constants.INTG_URL, props.getProperty(Constants.INTG_URL));
				con.setRequestProperty("Host", url.getHost());
				con.setRequestProperty("Authorization", accessToken);
				con.setRequestProperty("content-type", props.getProperty(Constants.WS_CONTENT_TYPE,Defaults.CONTENTTYPE));
				con.setRequestProperty("X-Client-ID", props.getProperty(Constants.OAUTHSERVER_CLIENT_ID,Defaults.OAUTHCLIENT_ID));
				con.setRequestProperty("X-Client-Secret", props.getProperty(Constants.OAUTHSERVER_CLIENT_SECRET,Defaults.OAUTHCLIENT_SECRET));
				con.setRequestProperty("X-AUTHORIZATION", accessToken);
				}
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				wr = new BufferedOutputStream(con.getOutputStream(), Defaults.WS_READ_BUFFER_SIZE);
				wr.write(inputMessage.getBytes());
				wr.flush();
				wr.close();
				responseCode = con.getResponseCode();
				log.info("\nSending 'POST' request to URL : " + url);
				log.info("Response Code : " + responseCode);
				if (responseCode == Constants.WS_RESPONSE_HTTP_OK)
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				else
					in = new BufferedReader(new InputStreamReader(con.getErrorStream()));

				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					log.debug("resp is " + inputLine);
					response.append(inputLine);
				}
				log.info(response.toString());
				String jsonStringResponse = response.toString();
				HashMap<String, String> resp = new HashMap<String, String>();
				resp.put(Constants.WS_RESPONSE_KEY1, responseCode + "");
				resp.put(Constants.WS_RESPONSE_KEY2, "success");
				resp.put(Constants.WS_RESPONSE_KEY3, response.toString());
				return new JSONObject(resp);

			} catch (IOException e) {
				if (Constants.WS_TIMEOUT_EXP_MESSAGE_COMPARATOR.equals(e.getMessage())) {
					log.error(e.getMessage(), e);
					throw new Exception(Constants.WS_TIMEOUT_CUSTOM_EXCEPTION_MESAGE, e);
				}
			} catch (Exception e) {

				log.error(Constants.WS_GENERIC_CUSTOM_EXCEPTION_MESAGE + responseCode + " Detail Response is "
						+ response);
				throw e;
			}

			finally {
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
		}

		else {
			log.error("-------Access token not received---------throwing Exception..");
			throw new Exception(Constants.OAUTH_GENERIC_CUSTOM_EXCEPTION_MESAGE + accessToken);
		}
		HashMap<String, String> resp = new HashMap<String, String>();
		resp.put(Constants.WS_RESPONSE_KEY1, responseCode + "");
		resp.put(Constants.WS_RESPONSE_KEY2, "ERROR");
		resp.put(Constants.WS_RESPONSE_KEY3, response.toString());
		return new JSONObject(resp);

	}

	public String getOauthToken() throws Exception {

		String token = authenticater.getAccessToken();
		return token;
	}

}