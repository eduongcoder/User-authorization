package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.request.PermissionRequest;
import com.example.dto.respone.ApiRespone;
import com.example.dto.respone.PermissionRespone;
import com.example.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionController {
	PermissionService permissionService;

	@PostMapping("/create")
	private ApiRespone<PermissionRespone> create(@RequestBody PermissionRequest request) {
		return ApiRespone.<PermissionRespone>builder()
				.result(permissionService.create(request))
				.build();
	}
	
	@GetMapping("/getAll")
	private ApiRespone<List<PermissionRespone>> getAll() {
		return ApiRespone.<List<PermissionRespone>>builder()
				.result(permissionService.getAll())
				.build();
	}
	
	@DeleteMapping("/{permission}")
	private ApiRespone<Void> deletePermission(@PathVariable String permission) {
		permissionService.delete(permission);
		
		return ApiRespone.<Void>builder().build();
	}
}
