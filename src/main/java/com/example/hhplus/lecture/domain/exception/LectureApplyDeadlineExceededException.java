package com.example.hhplus.lecture.domain.exception;

import com.example.hhplus.exception.CustomException;

public class LectureApplyDeadlineExceededException extends CustomException {
	public LectureApplyDeadlineExceededException() {
		super(400, "특강 신청 기한이 초과하였습니다.");
	}
}
