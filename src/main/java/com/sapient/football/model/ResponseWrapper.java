package com.sapient.football.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Wrapper class for the api response
 * @author alpha
 *
 */
public class ResponseWrapper {
	
	private final Map<String, Object> response = new LinkedHashMap<String, Object>();

	public static ResponseWrapper success(Object data) {
		ResponseWrapper wrapper = new ResponseWrapper();
		wrapper.response.put("status", "success");
		wrapper.response.put("data", data);
		return wrapper;
	}
	
	public static ResponseWrapper error(Object data) {
		ResponseWrapper wrapper = new ResponseWrapper();
		wrapper.response.put("status", "error");
		wrapper.response.put("data", data);
		return wrapper;
	}
	
	public Map<String, Object> getResponse() {
		return response;
	}
}
