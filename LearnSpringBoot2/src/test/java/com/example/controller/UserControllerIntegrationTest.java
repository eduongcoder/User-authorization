package com.example.controller;



import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.LearnSpringBoot2Application;
import com.example.dto.request.UserCreationRequest;
import com.example.dto.respone.UserRespone;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = LearnSpringBoot2Application.class)
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

	@Container
	static final MySQLContainer<?> MY_SQL_CONTAINER=new MySQLContainer<>("mysql:latest");
	
	@DynamicPropertySource
	static void configureDatasource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> MY_SQL_CONTAINER.getJdbcUrl());
		registry.add("spring.datasource.username", () -> MY_SQL_CONTAINER.getUsername());
		registry.add("spring.datasource.password", () -> MY_SQL_CONTAINER.getPassword());
		registry.add("spring.datasource.driver-class-name",  () -> "com.mysql.cj.jdbc.Driver");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");

	}
	
	@Autowired
	private MockMvc mockMvc;
	
	
	private UserCreationRequest request;
	private UserRespone userRespone;
	private LocalDate dob;
	
	@BeforeEach
	void initData() {
		dob=LocalDate.of(1990, 1, 1);
		
		request =UserCreationRequest.builder()
				.username("johnjohnjohn")
				.firstname("john")
				.lastname("Doe")
				.password("123456")
				.dob(dob)
				.build();
		
		userRespone=UserRespone.builder()
				.id("dda2fd7d26f5")
				.username("johnjohnjohn")
				.firstname("john")
				.lastname("Doe")
				.dob(dob)
				.build();
				
	}
	
	@Test
	void createUser_validRequest_success() throws Exception {
		//GIVEN
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String content=objectMapper.writeValueAsString(request);
	
//		Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userRespone);
		
		//WHEN, THEN
		var respone= mockMvc.perform(MockMvcRequestBuilders
				.post("/User/users")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.username").value("johnjohnjohn"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.firstname").value("john"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.lastname").value("Doe"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.dob").value(dob.toString()));
			
		log.info("Respone: "+ respone.andReturn().getResponse().getContentAsString());
		
	}


	
}
