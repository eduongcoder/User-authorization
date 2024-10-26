package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.request.RoleRequest;
import com.example.dto.respone.ApiRespone;
import com.example.dto.respone.RoleRespone;
import com.example.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class RoleController {

	RoleService roleService;
	
	@PostMapping("/create")
	ApiRespone<RoleRespone> create(@RequestBody RoleRequest request) {
		return ApiRespone.<RoleRespone>builder()
				.result(roleService.create(request))
				.build();
	}
	
	@GetMapping("/getAll")
	ApiRespone<List<RoleRespone>> getAll() {
		return ApiRespone.<List<RoleRespone>>builder()
				.result(roleService.getAll())
				.build();
	}
	
	@DeleteMapping("/{role}")
	ApiRespone<Void> deletePermission(@PathVariable String role) {
		roleService.delete(role);
		
		return ApiRespone.<Void>builder().build();
	}
}
