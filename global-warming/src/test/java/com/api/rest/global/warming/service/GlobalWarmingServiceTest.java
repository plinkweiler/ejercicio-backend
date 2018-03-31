package com.api.rest.global.warming.service;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.api.rest.global.warming.main.Application;
import com.api.rest.global.warming.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@AutoConfigureMockMvc
public class GlobalWarmingServiceTest {
	
	@MockBean
	RestTemplate restTemplate;
	
	@Autowired
	HttpHeaders httpHeaders;

	@Autowired
	private GlobalWarmingService globalWarmingService;

	@Test
	public void testGetRepoInfo() throws Exception {
		
		String json = " [{\"id\":127353658,\"name\":\"ejercicio-backend\",\"full_name\":\"plinkweiler/ejercicio-backend\"}]";
		
		when(restTemplate.getForObject(Constants.USER_URI + "plinkweiler/repos", String.class)).thenReturn(json);
		
		JSONArray jsonArray = globalWarmingService.getRepoInfo(Constants.USER_URI + "plinkweiler/repos");
		
		JSONAssert.assertEquals(json, jsonArray, false);
	}
	
	@Test
	public void testGetUserLocation() throws Exception {
		
		String json = "{\"login\": \"plinkweiler\","
				+ "\"id\": 37916047,"
				+ "\"location\": \"Buenos Aires\","
				+ "\"email\": null,"
				+ "\"hireable\": null,"
				+ "\"created_at\": \"2018-03-29T22:22:41Z\","
				+ "\"updated_at\": \"2018-03-30T04:27:22Z\"}";
		
		when(restTemplate.getForObject(Constants.USER_URI + "plinkweiler", String.class)).thenReturn(json);
		
		String location = globalWarmingService.getUserLocation("plinkweiler");
		
		Assert.assertEquals("Buenos Aires", location);
	}
	
	@Test
	public void testGetAverageTemperature() throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fecha_creacion_repo", "2018-03-29T22:22:41Z");
		jsonObject.put("promedio", "29.5");
		
		JSONArray jsonArray = new JSONArray();
	    jsonArray.put(jsonObject);
		
		String json = "{  "
				+"\"data\":{  "
				+"     \"weather\":[  "
				+"         {  "
				 +"           \"maxtempC\":\"36\","
				 +"           \"maxtempF\":\"97\","
				  +"          \"mintempC\":\"23\","
				  +"          \"mintempF\":\"73\","
				  +"          \"totalSnow_cm\":\"0.0\","
				   +"         \"sunHour\":\"11.5\","
				    +"        \"uvIndex\":\"0\""
				    +"     }"
				    +"  ]"
				    +" } "
				    +"	}";
		
		String jsonStringArray = "[{\"login\": \"plinkweiler\","
				+ "\"id\": 37916047,"
				+ "\"location\": \"Buenos Aires\","
				+ "\"email\": null,"
				+ "\"hireable\": null,"
				+ "\"created_at\": \"2018-03-29T22:22:41Z\","
				+ "\"updated_at\": \"2018-03-30T04:27:22Z\"}]";
		
		
		final String weatherUri = Constants.WEATHER_URI +"&q=Buenos Aires&format=json&date=2018-03-29T22:22:41Z";	
		when(restTemplate.getForObject(weatherUri, String.class)).thenReturn(json);
		JSONArray returnObject = new JSONArray(jsonStringArray);
		
		JSONArray result = globalWarmingService.getAverageTemperature("Buenos Aires", returnObject);
		
		JSONAssert.assertEquals(jsonArray, result, false);
	}

}
