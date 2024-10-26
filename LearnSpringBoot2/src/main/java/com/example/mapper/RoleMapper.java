package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.request.RoleRequest;
import com.example.dto.respone.RoleRespone;
import com.example.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	@Mapping(target = "permissions",ignore = true)
	Role toRole(RoleRequest request);
	RoleRespone toRoleRespone(Role role);
}
