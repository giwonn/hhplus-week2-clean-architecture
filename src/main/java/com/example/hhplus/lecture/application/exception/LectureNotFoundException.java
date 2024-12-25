package com.example.hhplus.lecture.application.exception;

import com.example.hhplus.exception.CustomException;

public class LectureNotFoundException extends CustomException {
	public LectureNotFoundException() {
		super(400, "특강이 존재하지 않습니다.");
	}
}
