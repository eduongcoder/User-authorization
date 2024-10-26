package com.example.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.LearnSpringBoot2Application;
import com.example.LearnSpringBootApplication;
import com.example.dto.request.UserCreationRequest;
import com.example.dto.respone.UserRespone;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = LearnSpringBoot2Application.class)
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
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
	
		Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userRespone);
		
		//WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders
				.post("/User/users")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.id").value("dda2fd7d26f5"));
				
		
	}
	@Test
	void createUser_usernameInvalid_fail() throws Exception {
		//GIVEN
		
		request.setUsername("joh");
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String content=objectMapper.writeValueAsString(request);
	
		
		//WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders
				.post("/User/users")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1002))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("User name must at least 8 characters"));
				
		
	}

	
}
