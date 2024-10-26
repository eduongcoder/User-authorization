package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.request.UserCreationRequest;
import com.example.dto.request.UserUpdateRequest;
import com.example.dto.respone.ApiRespone;
import com.example.dto.respone.UserRespone;
import com.example.entity.User;
import com.example.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/User")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/users")
	public ApiRespone<UserRespone> createUser(@RequestBody @Valid UserCreationRequest request) {
		log.info("Controller: Create User");
		return ApiRespone.<UserRespone>builder().result(userService.createUser(request)).build();
	}

	@GetMapping("/getUsers")
	public ApiRespone<List<UserRespone>> getUsers() {

		return ApiRespone.<List<UserRespone>>builder()
			   .result(userService.getUsers()).build();
	}
  
	@GetMapping("/{id}") 
	public  ApiRespone<UserRespone> getUser(@PathVariable String id) {
		return ApiRespone.<UserRespone>builder().
				result(userService.getUser(id)).build(); 
	}  
	
	@GetMapping("/myInfo")
	public ApiRespone<UserRespone> getMyInfo() {
		return ApiRespone.<UserRespone>builder().
				result(userService.getMyInfo()).build();
	}

	@PutMapping("/{id}")
	public UserRespone updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {
		return userService.updateUser(id, request);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable String id) {
		userService.deleteUser(id);

		return "User đã bị xóa mất tiu ùi";
	}
}
