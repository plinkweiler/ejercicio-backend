package com.api.rest.global.warming.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.api.rest.global.warming.util.Constants;

@Service
public class GlobalWarmingService {
	
	@Autowired
	RestTemplate restTemplate;
	
	public String getUserLocation(String username) throws RestClientException{
		
		String location = "";
		try {
	    	String userInfo = restTemplate.getForObject(Constants.USER_URI + username, String.class);
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
	
	public JSONArray getRepoInfo(String uriUserRepos) throws RestClientException,JSONException{
		
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

	public JSONArray getAverageTemperature(String location, JSONArray jsonArray) throws RestClientException,JSONException{
		JSONArray returnObject = new JSONArray();
	    for(int i =0; i< jsonArray.length(); i++){
	    	String createdDate = "";
	    	try {
			    	if(jsonArray.get(i) instanceof JSONObject){
		                JSONObject jsnObj = (JSONObject)jsonArray.get(i);
		                createdDate = jsnObj.getString("created_at");
		                
		                final String weatherUri = Constants.WEATHER_URI +"&q="+location+"&format=json&date="+createdDate;	    
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
