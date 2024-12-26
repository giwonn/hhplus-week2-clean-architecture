package com.example.hhplus.lecture.domain.exception;

import com.example.hhplus.exception.CustomException;

public class LectureAlreadyAppliedExceededException extends CustomException {
	public LectureAlreadyAppliedExceededException() {
		super(400, "이미 신청한 특강입니다.");
	}
}
