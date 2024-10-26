package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
	USER_EXISTED(1001,"User existed",HttpStatus.BAD_REQUEST),
	USERNAME_INVALID(1002,"User name must at least {min} characters",HttpStatus.BAD_REQUEST),
	USERNAME_INVALIDPASSWORD(1003,"User password must at least {min} characters",HttpStatus.BAD_REQUEST)
	,INVALID_KEY(1004,"Invalid Key",HttpStatus.BAD_REQUEST),
	USER_NOT_EXISTED(1005,"User not existed",HttpStatus.NOT_FOUND),
	UNAUTHENTICATED(1006,"Unauthenticated",HttpStatus.UNAUTHORIZED)
	,UNAUTHORIZED(1007,"You do not have permission",HttpStatus.FORBIDDEN)
	,INVALID_DOB(1008,"Your age must be at least {min}",HttpStatus.BAD_REQUEST)
	,UNKNOWN_ERROR(9999,"Unknown Error",HttpStatus.INTERNAL_SERVER_ERROR);
	
	private int code;
	private String message;
	private HttpStatusCode statusCode;
	
	

	private ErrorCode(int code, String message,HttpStatusCode statusCode) {
		this.code = code;
		this.message = message;
		this.statusCode=statusCode;
	}
	
	
}
