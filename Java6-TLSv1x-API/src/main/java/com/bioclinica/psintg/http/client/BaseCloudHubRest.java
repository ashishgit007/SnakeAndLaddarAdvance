package com.bioclinica.psintg.http.client;

import java.util.Map;

import org.hornetq.utils.json.JSONObject;

/**
 * 
 * @author Ashish.Trivedi CloudHub Rest Proxy facade
 * used for backward compatibility
 */
public interface BaseCloudHubRest {
	public JSONObject postToCloudHubRestApi(String payload, String studyCode, Map<String, String> studyConfig) throws Exception;
}
