package com.bioclinica.psintg.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.bioclinica.psintg.http.client.CustomTLSSocketFactory;
/**
 * 
 * @author Ashish.Trivedi
 * This is added if this library is not enough to be used get you gneric handshaked HTTP connection
 * rest implemtor hs to deal with.
 *
 */
public class GenericConnection {
	public static HttpsURLConnection getHandShakedConnection(String webserviceURL, int readTimeOut, int port)
			throws IOException, KeyManagementException, NoSuchAlgorithmException
	{
		if (System.getProperty("java.version").contains(Defaults.JAVA_VERSION))
			System.setProperty("https.protocols", Defaults.JAVA_6_DEFAULT_TLS);
		HttpsURLConnection con = null;
		URL obj = new URL(webserviceURL);
		con = (HttpsURLConnection) obj.openConnection();
		CustomTLSSocketFactory tls = new CustomTLSSocketFactory(readTimeOut);
		tls.setHost(obj.getHost());
		tls.setPort(port);
		con.setSSLSocketFactory(tls);
		con.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		con.setRequestProperty("Host", obj.getHost());
		return con;
	}

}
