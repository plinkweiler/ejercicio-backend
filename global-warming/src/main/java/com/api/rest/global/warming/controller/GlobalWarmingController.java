package com.api.rest.global.warming.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/globalWarming")
public class GlobalWarmingController {
	
	static final String KEY = "8e087ae47efe4d4288e230637182302";
	
	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping(value = "getUserInfo/{user}", method = RequestMethod.GET)
	public ResponseEntity<String> getUserInfo(@PathVariable String user) {
		final String uriUserInfo = "https://api.github.com/users/" + user;

	    String userInfo = restTemplate.getForObject(uriUserInfo, String.class);
	    
	    JSONObject jsonUserInfo = new JSONObject(userInfo);
	    
	    String location = jsonUserInfo.getString("location");
	    
	    
	    final String uriUserRepos = uriUserInfo + "/repos";
	    
	    
	    String createdDate = "";
	    String repoUserInfo = restTemplate.getForObject(uriUserRepos, String.class);
	    
	    JSONArray jsonArray = new JSONArray(repoUserInfo);
	    JSONArray obj = new JSONArray();
	    for(int i =0; i< jsonArray.length(); i++){
	    	try {
	    	if(jsonArray.get(i) instanceof JSONObject){
                JSONObject jsnObj = (JSONObject)jsonArray.get(i);
                createdDate = (String)jsnObj.get("created_at");
                System.out.println("\nFechas de creacion: " + createdDate);
                
                final String weatherUri = "http://api.worldweatheronline.com/premium/v1/past-weather.ashx?key="+KEY+"&q="+location+"&format=json&date="+createdDate;	    
        	    String weatherInfo = restTemplate.getForObject(weatherUri, String.class);
        	    JSONObject jsonWeatherInfo = new JSONObject(weatherInfo);
        	    
        	    
        	    JSONArray jsonWeatherArray = new JSONArray(((JSONObject)jsonWeatherInfo.get("data")).get("weather").toString());
        	    
        	    
        	    JSONObject jsnWeatherObj = (JSONObject)jsonWeatherArray.get(0);
        	    Double tempMax = Double.valueOf(jsnWeatherObj.getString("maxtempC"));
        	    Double tempMin = Double.valueOf(jsnWeatherObj.getString("mintempC"));
        	    
        	    Double tempProm = (tempMax + tempMin) / 2;
        	    
        	    System.out.println("información temperatura promedio: " + tempProm.toString());
        	    JSONObject output = new JSONObject();
        	    output.put("created_date", createdDate);
        	    output.put("tempProm", tempProm.toString());
        	    
        	    obj.put(output);
	    	}
	    
	    	}catch (JSONException e) {
					e.printStackTrace();
					JSONObject output = new JSONObject();
	        	    output.put("tempProm", "No hay información del clima");
	        	    output.put("created_date", createdDate);
	        	    obj.put(output);
	    	}
	    }
	    
	    JSONObject outputJsonObj = new JSONObject();
	    outputJsonObj.put("reposCount", jsonArray.length());
	    outputJsonObj.put("prom", obj);
	    
	    return new ResponseEntity<String>(outputJsonObj.toString(), HttpStatus.OK);
		
	}   
	    
}
