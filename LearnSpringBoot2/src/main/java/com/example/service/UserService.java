package com.example.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.request.UserCreationRequest;
import com.example.dto.request.UserUpdateRequest;
import com.example.dto.respone.UserRespone;
import com.example.entity.User;
import com.example.enums.Role;
import com.example.exception.AppException;
import com.example.exception.ErrorCode;
import com.example.mapper.UserMapper;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

	UserRepository userRepository;

	UserMapper userMapper;

	PasswordEncoder passwordEncoder;

	RoleRepository roleRepository;
	public UserRespone createUser(UserCreationRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}

		User user = userMapper.toUser(request);

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		HashSet<String> roles = new HashSet<>();
		roles.add(Role.USER.name());

//		user.setRoles(roles);
		user = userRepository.save(user);

		return userMapper.toUserRespone(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
//	@PreAuthorize("hasAuthority('APPROVE_POST')")
	public List<UserRespone> getUsers() {
		log.info("In method get User");
		return userRepository.findAll().stream().map(t -> userMapper.toUserRespone(t)).toList();
	}

	@PostAuthorize("returnObject.username == authentication.name")
	public UserRespone getUser(String id) {
		log.info("In method get User by id");
		return userMapper.toUserRespone(
				userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
	}

	public UserRespone getMyInfo() {
		var context= SecurityContextHolder.getContext();
		String name=context.getAuthentication().getName();
		log.info("Name ne Duong: ",name);
		User byUserName= userRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
	
		return userMapper.toUserRespone(byUserName) ;		
	}
  
	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}

	public UserRespone updateUser(String id, UserUpdateRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

		userMapper.updateUser(user, request);
		
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		var roles=roleRepository.findAllById(request.getRoles());
		
		user.setRoles(new HashSet<>(roles));
		
		return userMapper.toUserRespone(userRepository.save(user));
	}
}
