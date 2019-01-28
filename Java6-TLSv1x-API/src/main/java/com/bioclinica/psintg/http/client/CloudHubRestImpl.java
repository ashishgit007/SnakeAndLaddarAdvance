package com.bioclinica.psintg.http.client;

import java.util.Map;

import org.apache.log4j.Logger;
import org.hornetq.utils.json.JSONObject;
import com.bioclinica.psintg.commons.Constants;
import com.tsi9.tig.intg.muleApi.facade.IcloudHubRest;

/**
 * 
 * @author Ashish.Trivedi 
 * This class is Implementation of main facade
 * 
 */
public class CloudHubRestImpl  extends CustomSender implements IcloudHubRest{
	
	protected static Logger log = Logger.getLogger("CloudHubRestImpl");
	@Override
	public org.hornetq.utils.json.JSONObject customPost(String payload, String studyCode, Map<String, String> studyConfig) throws Exception {		
		log.info("size of studyConfig is ------ " +studyConfig.size());				
		CloudhubRestHelper cloudhubRestHelper = new CloudhubRestHelper(Constants.FAN_IN_COMPATIBILITY_STRING, Constants.FAN_IN_COMPATIBILITY_STRING,Integer.parseInt(Constants.FAN_IN_COMPATIBILITY_INT), studyConfig);
		cloudhubRestHelper.setInputMessage(payload);		
		JSONObject json = cloudhubRestHelper.sendPost();
		System.out.println("jsonString  is "+json);		
		return json;
	}

	@Override
	public JSONObject postToCloudHubRestApi(String payload, String studyCode, Map<String, String> studyConfig)
			throws Exception {		
		
		log.info("size of studyConfig is ------ " +studyConfig.size());				
		CloudhubRestHelper cloudhubRestHelper = new CloudhubRestHelper(Constants.FAN_IN_COMPATIBILITY_STRING, Constants.FAN_IN_COMPATIBILITY_STRING,
				Integer.parseInt(Constants.FAN_IN_COMPATIBILITY_INT), studyConfig);
		cloudhubRestHelper.setInputMessage(payload);		
		JSONObject json  = cloudhubRestHelper.sendPost();				
		json.put("Status", json.toString());		
		return json;
	
	}
}
