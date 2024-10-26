package com.example.mapper;

import org.mapstruct.Mapper;

import com.example.dto.request.PermissionRequest;

import com.example.dto.respone.PermissionRespone;

import com.example.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper{
	Permission toPermission(PermissionRequest request);
	PermissionRespone toPermissionRespone(Permission permission); 
}
