package com.example.hhplus.exception;

public abstract class CustomException extends RuntimeException {
	public final int errorCode;

	protected CustomException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
