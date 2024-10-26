package com.example.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.LearnSpringBoot2Application;
import com.example.dto.request.UserCreationRequest;
import com.example.dto.respone.UserRespone;
import com.example.entity.User;
import com.example.exception.AppException;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



@SpringBootTest(classes = LearnSpringBoot2Application.class)
@TestPropertySource("/test.properties")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	private UserCreationRequest request;
	private UserRespone userRespone;
	private User user;
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
	
		user=User.builder()
				.id("dda2fd7d26f5")
				.username("johnjohnjohn")
				.firstname("john")
				.lastname("Doe")
				.dob(dob)
				.build();
	}
	
	@Test
	void createUser_validRequest_success() {
		//GIVEN
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userRepository.save(any())).thenReturn(user);
		
		//WHEN
		var respone=userService.createUser(request);
		
		//THEN
		Assertions.assertThat(respone.getId()).isEqualTo("dda2fd7d26f5");
		Assertions.assertThat(respone.getUsername()).isEqualTo("johnjohnjohn");

	}
	@Test
	void createUser_userExists_fail() {
		//GIVEN
		when(userRepository.existsByUsername(anyString())).thenReturn(true);
		
		//WHEN
		var exepction= assertThrows(AppException.class, () -> userService.createUser(request));
	
		Assertions.assertThat(exepction.getErroCode().getCode()).isEqualTo(1001);
	}
	@Test
	@WithMockUser(username = "johnjohnjohn")
	void getMyInfo_valid_success() {
		//GIVEN
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		
		var respone=userService.getMyInfo();
		
		//THEN
		Assertions.assertThat(respone.getUsername()).isEqualTo("johnjohnjohn");
		Assertions.assertThat(respone.getId()).isEqualTo("dda2fd7d26f5");

	} 
	@Test
	@WithMockUser(username = "johnjohnjohn")
	void getMyInfo_userNotFound_error() {
		//GIVEN
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));
		 
		var exepction= assertThrows(AppException.class,
				() -> userService.getMyInfo());
	
		//THEN
		Assertions.assertThat(exepction.getErroCode().getCode()).isEqualTo(1005);
 
	} 
}
