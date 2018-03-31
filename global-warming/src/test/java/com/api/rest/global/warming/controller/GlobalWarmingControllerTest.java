package com.api.rest.global.warming.controller;

import static org.mockito.BDDMockito.given;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.core.Is.is;

import com.api.rest.global.warming.main.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@AutoConfigureMockMvc
public class GlobalWarmingControllerTest {
	
	@Autowired
	HttpHeaders httpHeaders;
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private GlobalWarmingController globalWarmingController;

	@Test
	public void testGetUserInfo() throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fecha_creacion_repo", "2018-03-29T22:25:24Z");
		jsonObject.put("promedio", "29.5");
		
		JSONArray jsonArray = new JSONArray();
	    jsonArray.put(jsonObject);
		
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("cantidad_repos", 3);
		outputJsonObj.put("temperatura_promedio", jsonArray);
		
		ResponseEntity<String> response = new ResponseEntity<String>(outputJsonObj.toString(),httpHeaders,HttpStatus.OK);
		
		given(globalWarmingController.getUserInfo("plinkweiler")).willReturn(response);
		
		mvc.perform(get("/globalWarming/getUserInfo/plinkweiler")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content()
			      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			      .andExpect(jsonPath("$.cantidad_repos", is(3)))
				  .andExpect(jsonPath("$.temperatura_promedio[0].promedio", is("29.5")));
		
	}

}
