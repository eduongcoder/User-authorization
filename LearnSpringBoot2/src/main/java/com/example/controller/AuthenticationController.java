package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.request.AuthenticationRequest;
import com.example.dto.request.IntrospectRequest;
import com.example.dto.request.LogoutRequest;
import com.example.dto.request.RefreshRequest;
import com.example.dto.respone.ApiRespone;
import com.example.dto.respone.AuthenticationRespone;
import com.example.dto.respone.IntrospectRespone;
import com.example.service.AuthenticationServce;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

	AuthenticationServce authenticationServce;

	@PostMapping("/token")
	public ApiRespone<AuthenticationRespone> authenticate(@RequestBody AuthenticationRequest request) {

		var result = authenticationServce.authenticated(request);
		return ApiRespone.<AuthenticationRespone>builder().result(result).build();
	}

	@PostMapping("/introspect")
	public ApiRespone<IntrospectRespone> authenticate(@RequestBody IntrospectRequest request)
			throws JOSEException, ParseException {

		var result = authenticationServce.introspectRespone(request);
		return ApiRespone.<IntrospectRespone>builder().result(result).build();
	}
	@PostMapping("/logout")
	public ApiRespone<Void> logout(@RequestBody LogoutRequest request)
			throws JOSEException, ParseException {

		 authenticationServce.logout(request);
		return ApiRespone.<Void>builder().build();
	}
	
	@PostMapping("/refresh")
	public ApiRespone<AuthenticationRespone> refresh(@RequestBody RefreshRequest request) throws JOSEException, ParseException {

		var result = authenticationServce.refreshToken(request);
		return ApiRespone.<AuthenticationRespone>builder().result(result).build();
	}
}
	