package com.bioclinica.psintg.http.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bioclinica.psintg.commons.Constants;
import com.bioclinica.psintg.commons.Defaults;

/**
 * 
 * @author Ashish.Trivedi CloudHub Rest Proxy facade
 */
abstract public class CustomSender {
	protected static Logger log = Logger.getLogger("CustomSender");

	public CustomSender(){
		validate();		
	}

	public abstract org.hornetq.utils.json.JSONObject customPost(String payload, String studyCode,
			Map<String, String> studyConfig) throws Exception;

	public void validate() {
		if (System.getProperty("java.version").contains(Defaults.JAVA_VERSION))
			System.setProperty("https.protocols", Defaults.JAVA_6_DEFAULT_TLS);

	}

}
