package com.api.rest.global.warming.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.api.rest.global.warming.service.GlobalWarmingService;

@RestController
@RequestMapping(value = "/globalWarming")
public class GlobalWarmingController {
	
	private Map<String, JSONObject> info = new ConcurrentHashMap<String, JSONObject>();
	static final String USER_URI = "https://api.github.com/users/";
	
	@Autowired
	HttpHeaders httpHeaders;
	
	@Autowired
	GlobalWarmingService service;
	
	@RequestMapping(value = "/getAllInfo", method = RequestMethod.GET)
	public ResponseEntity<String> getAllInfo() {
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(info);
		
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("datos recopilados", jsonArray);
		return new ResponseEntity<String>(outputJsonObj.toString(),httpHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getUserInfo/{username}", method = RequestMethod.GET)
	public ResponseEntity<String> getUserInfo(@PathVariable String username) {
		
		JSONArray jsonArray = new JSONArray();
		JSONArray obj = new JSONArray();
		
		try {
			
			String location = service.getUserLocation(username);
			if(!"ERROR".equals(location)) {
				final String uriUserRepos = USER_URI + username + "/repos";
				jsonArray = service.getRepoInfo(uriUserRepos);
				obj = service.getAverageTemperature(location, jsonArray);
			}else {
				JSONObject outputJsonObj = new JSONObject();
			    outputJsonObj.put("error", "No se pudo determinar la ubicación del usuario");
			    info.put(username, outputJsonObj);
			    return new ResponseEntity<String>(outputJsonObj.toString(),httpHeaders,HttpStatus.OK);
			}
			
		}catch(RestClientException e) {
			e.printStackTrace();
			JSONObject outputJsonObj = new JSONObject();
		    outputJsonObj.put("error", "A ocurrido un error de comunicación con las APIs externas");
		    info.put(username, outputJsonObj);
		    return new ResponseEntity<String>(outputJsonObj.toString(),httpHeaders,HttpStatus.OK);
		}
	    
		JSONObject outputJsonObj = new JSONObject();
	    outputJsonObj.put("Cantidad de repos", jsonArray.length());
	    outputJsonObj.put("Temperatura promedio", obj);
	    info.put(username, outputJsonObj);
	    
	    return new ResponseEntity<String>(outputJsonObj.toString(),httpHeaders,HttpStatus.OK);
		
	}
}
