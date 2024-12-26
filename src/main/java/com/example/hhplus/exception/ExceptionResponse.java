package com.example.hhplus.exception;

record ExceptionResponse(
		int errorCode,
		String message
) {
	static ExceptionResponse from(CustomException ex) {
		return new ExceptionResponse(ex.errorCode, ex.getMessage());
	}
}
