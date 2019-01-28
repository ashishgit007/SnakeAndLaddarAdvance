package com.tsi9.tig.intg.muleApi.facade;

import java.util.Map;

import org.hornetq.utils.json.JSONObject;

/**
 * 
 * @author Ashish.Trivedi CloudHub Rest Proxy facade

 */
public interface IcloudHubRest {	
	public JSONObject postToCloudHubRestApi(String payload, String studyCode, Map<String, String> studyConfig) throws Exception;
}
