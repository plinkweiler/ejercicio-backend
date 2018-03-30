package com.api.rest.global.warming.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GlobalWarmingService {
	
	static final String KEY = "8e087ae47efe4d4288e230637182302";
	static final String WEATHER_URI = "http://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=" + KEY;
	static final String USER_URI = "https://api.github.com/users/";
	
	@Autowired
	RestTemplate restTemplate;
	
	public String getUserLocation(String username) throws RestClientException{
		
		String location = "";
		try {
	    	String userInfo = restTemplate.getForObject(USER_URI + username, String.class);
	    	JSONObject jsonUserInfo = new JSONObject(userInfo);
	    	location = jsonUserInfo.getString("location");
		}catch (JSONException e) {
			e.printStackTrace();
			location = "ERROR";
		}catch(RestClientException e) {
			e.printStackTrace();
			throw e;
		}
	    
	    return location;
	}
	
	public JSONArray getRepoInfo(String uriUserRepos) throws RestClientException{
		
		JSONArray jsonArray = new JSONArray();
		try {
			String repoUserInfo = restTemplate.getForObject(uriUserRepos, String.class);
		    jsonArray = new JSONArray(repoUserInfo);
		}catch(RestClientException e) {
			e.printStackTrace();
			throw e;
		}
	    return jsonArray;
	}

	public JSONArray getAverageTemperature(String location, JSONArray jsonArray) throws RestClientException{
		JSONArray returnObject = new JSONArray();
	    for(int i =0; i< jsonArray.length(); i++){
	    	String createdDate = "";
	    	try {
			    	if(jsonArray.get(i) instanceof JSONObject){
		                JSONObject jsnObj = (JSONObject)jsonArray.get(i);
		                createdDate = jsnObj.getString("created_at");
		                
		                final String weatherUri = WEATHER_URI +"&q="+location+"&format=json&date="+createdDate;	    
		        	    String weatherInfo = restTemplate.getForObject(weatherUri, String.class);
		        	    JSONObject jsonWeatherInfo = new JSONObject(weatherInfo);
		        	    
		        	    JSONArray jsonWeatherArray = new JSONArray(((JSONObject)jsonWeatherInfo.get("data")).get("weather").toString());
		        	    
		        	    JSONObject jsnWeatherObj = (JSONObject)jsonWeatherArray.get(0);
		        	    Double tempMax = Double.valueOf(jsnWeatherObj.getString("maxtempC"));
		        	    Double tempMin = Double.valueOf(jsnWeatherObj.getString("mintempC"));
		        	    
		        	    Double tempProm = (tempMax + tempMin) / 2;
		        	    
		        	    
		        	    JSONObject output = new JSONObject();
		        	    output.put("fecha_creacion_repo", createdDate);
		        	    output.put("promedio", tempProm.toString());
		        	    
		        	    returnObject.put(output);
			    	}
			    
	    	}catch (JSONException e) {
					e.printStackTrace();
					JSONObject output = new JSONObject();
	        	    output.put("promedio", "No hay información del clima");
	        	    output.put("fecha_creacion", createdDate);
	        	    returnObject.put(output);
	    	}catch(RestClientException e) {
	    		e.printStackTrace();
	    		throw e;
	    	}
	    }
		return returnObject;
	}   

}
