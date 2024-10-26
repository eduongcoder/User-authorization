package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.request.PermissionRequest;
import com.example.dto.respone.PermissionRespone;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

	PermissionRepository permissionRepository;
	PermissionMapper permissionMapper;

	public PermissionRespone create(PermissionRequest request) {
		log.info(request.toString());
		Permission permission = permissionMapper.toPermission(request);
		permission = permissionRepository.save(permission);
		PermissionRespone respone = permissionMapper.toPermissionRespone(permission);

		return respone;
	}
	public List<PermissionRespone> getAll(){
		var permission=permissionRepository.findAll();
		return permission.stream().map(t -> permissionMapper.toPermissionRespone(t)).toList();
	}
	
	public void delete(String permission) {
		permissionRepository.deleteById(permission);
	}
}
