package com.example.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.request.RoleRequest;
import com.example.dto.respone.RoleRespone;
import com.example.mapper.RoleMapper;
import com.example.repository.PermissionRepository;
import com.example.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {

	RoleRepository rolerepository;
	RoleMapper roleMapper;
	PermissionRepository permissionRepository;

	public RoleRespone create(RoleRequest request) {
		var role = roleMapper.toRole(request);
		log.info(role.getDescription());

		var permission = permissionRepository.findAllById(request.getPermissions());

		role.setPermissions(new HashSet<>(permission));
		role = rolerepository.save(role);

		return roleMapper.toRoleRespone(role);
	}
	
	public List<RoleRespone> getAll(){
		return rolerepository.findAll()
				.stream()
				.map(t -> roleMapper.toRoleRespone(t))
				.toList();
	}
	
	public void delete(String id) {
		rolerepository.deleteById(id);
	}
}
