package com.example.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.example.validator.DobContraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
	String password;
	String firstname;
	String lastname;
	@DobContraint(min = 18,message = "INVALID_DOB")
	LocalDate dob;
	List<String>roles;   
 
}
