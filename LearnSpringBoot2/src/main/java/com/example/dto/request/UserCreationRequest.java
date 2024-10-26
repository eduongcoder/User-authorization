package com.example.dto.request;

import java.time.LocalDate;

import com.example.validator.DobContraint;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
	String id;

	@Size(min = 8, message = "USERNAME_INVALID")
	String username;
	@Size(min = 6, message = "USERNAME_INVALIDPASSWORD")
	String password;
	String firstname;
	String lastname;
	
	@DobContraint(min = 16,message = "INVALID_DOB")
	LocalDate dob;

}
