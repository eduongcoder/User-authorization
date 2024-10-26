package com.example.exception;

public class AppException extends RuntimeException {

	private ErrorCode erroCode;

	public ErrorCode getErroCode() {
		return erroCode;
	}

	public void setErroCode(ErrorCode erroCode) {
		this.erroCode = erroCode;
	}

	public AppException(ErrorCode erroCode) {
		super(erroCode.getMessage());
		this.erroCode = erroCode;
	}

}
