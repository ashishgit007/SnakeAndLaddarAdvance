package com.bioclinica.psintg.commons;
/**
 * 
 * @author Ashish.Trivedi
 *This is used to encapsulet all properties (edcconfig)
 */
public class Constants {
	
	public static final String OAUTH_CONNECTION_TIMEOUT_MS =  "oauth_conn_timeOut";
	public static final String OAUTH_READTIMEOUT_MS =  "oauth_read_timeOut";
	public static final String WS_CONNECTION_TIMEOUT_MS_1 =  "connectionTimeOut";
	public static final String WS_CONNECTION_TIMEOUT_MS_2 =  "HttpConnectionTimeout";
	public static final String WS_READTIMEOUT_MS_1 =  "readTimeOut";
	public static final String WS_READTIMEOUT_MS_2	 =  "HttpSocketTimeout";
	public static final String OAUTHSERVER_URI =  "oAuthEndPoint";
	public static final String OAUTHSERVER_REFRESH_TOKEN =  "oAuthRefresh_token";
	public static final String OAUTHSERVER_CLIENT_ID =  "oAuthClient_id";
	public static final String OAUTHSERVER_CLIENT_SECRET =  "oAuthClient_secret";
	public static final String WS_HOST="Mulehost";
	public static final String MULE_ENDPOINT="MuleRestEndpoint";
	public static final String WS_HOST_PORT="Sslport";
	public static final String INTG_URL="Intg-URL"; 
	public static final String WS_URL="wsURL";
	
	
	public static final String WS_CONTENT_TYPE="contentType";
	public static final String WS_AUTH_SCHEME="grant_flow_type";
	
	
	public static final String WS_VERB="POST";
	public static final int WS_RESPONSE_HTTP_OK=200;
	//public static final String WS_VERB="POST";
	
	public static final String FAN_IN_COMPATIBILITY_STRING= "NA";
	public static final String FAN_IN_COMPATIBILITY_INT= "0";
	public static final String WS_RESPONSE_KEY1= "httpstatuscode";
	public static final String WS_RESPONSE_KEY2= "responseMessage";
	public static final String WS_RESPONSE_KEY3= "responseMessageDetail";
	public static final String WS_TIMEOUT_EXP_MESSAGE_COMPARATOR= "Internal TLS error, this could be an attack ";
	public static final String WS_TIMEOUT_CUSTOM_EXCEPTION_MESAGE= "TimedOut connecting to External Service ";
	public static final String WS_GENERIC_CUSTOM_EXCEPTION_MESAGE= "Generic Exception occured  HTTP status Code is ";
	public static final String OAUTH_GENERIC_CUSTOM_EXCEPTION_MESAGE= " Unable to proceed as OAUTH token is Empty ";
	
	
	
	
	
		
	

}
