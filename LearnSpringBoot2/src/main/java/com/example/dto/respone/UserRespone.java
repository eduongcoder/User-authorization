package com.example.dto.respone;

import java.time.LocalDate;
import java.util.Set;

import com.example.entity.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRespone {

	String id;
	String username;
	String firstname;
	String lastname;
	LocalDate dob;
	Set<RoleRespone> roles;
}
