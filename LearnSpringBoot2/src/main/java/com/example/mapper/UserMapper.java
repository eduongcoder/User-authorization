package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.dto.request.UserCreationRequest;
import com.example.dto.request.UserUpdateRequest;
import com.example.dto.respone.UserRespone;
import com.example.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User toUser(UserCreationRequest request);
	@Mapping(target = "roles",ignore = true)
	void updateUser(@MappingTarget User user,UserUpdateRequest request);
	UserRespone toUserRespone(User user); 
}
