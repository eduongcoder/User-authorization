package com.example.exception;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.header.writers.frameoptions.StaticAllowFromStrategy;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.dto.respone.ApiRespone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private static final String MIN_ATTRIBUTE="min";
	
	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiRespone> handlingRuntimeException(RuntimeException exception) {

		ApiRespone apiRespone = ApiRespone.builder().build();

		apiRespone.setCode(ErrorCode.UNKNOWN_ERROR.getCode());
		apiRespone.setMessage(ErrorCode.UNKNOWN_ERROR.getMessage());

		return ResponseEntity.badRequest().body(apiRespone);
	}

	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiRespone> handlingAppException(AppException exception) {
		ErrorCode erroCode = exception.getErroCode();
		ApiRespone apiRespone = ApiRespone.builder().build();

		apiRespone.setCode(erroCode.getCode());
		apiRespone.setMessage(erroCode.getMessage());

		return ResponseEntity.status(erroCode.getStatusCode()).body(apiRespone);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiRespone> handlingValidation(MethodArgumentNotValidException exception) {

		String enumKey = exception.getFieldError().getDefaultMessage();
		ErrorCode erroCode = ErrorCode.INVALID_KEY;

		Map<String, Object> attributes =null;
		try {
			erroCode = ErrorCode.valueOf(enumKey);

			var constraintViolation = exception.getBindingResult().getAllErrors().get(0)
					.unwrap(ConstraintViolation.class);

			 attributes= constraintViolation.getConstraintDescriptor().getAttributes();
		
			log.info(attributes.toString());
		} catch (IllegalArgumentException e) {

		}

		ApiRespone apiRespone = ApiRespone.builder().build();

		apiRespone.setCode(erroCode.getCode());
		apiRespone.setMessage(Objects.nonNull(attributes)? 
				mapAttribute(erroCode.getMessage(), attributes):erroCode.getMessage());

		return ResponseEntity.status(erroCode.getStatusCode()).body(apiRespone);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiRespone> handlingAccessDeniedException(AccessDeniedException exception) {

		ErrorCode erroCode = ErrorCode.UNAUTHORIZED;

		return ResponseEntity.status(erroCode.getStatusCode())
				.body(ApiRespone.builder().code(erroCode.getCode()).message(erroCode.getMessage()).build());
	}

	private String mapAttribute(String message,Map<String, Object> attribute) {
		String minValue=String.valueOf(attribute.get(MIN_ATTRIBUTE)) ;
	
		return message.replace("{"+MIN_ATTRIBUTE+"}",minValue);
	
	}
}
