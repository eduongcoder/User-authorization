package com.example.dto.respone;

import java.util.Set;

import com.example.entity.Permission;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRespone {
	String name;
	String description;

	Set<PermissionRespone> permissions;
}
