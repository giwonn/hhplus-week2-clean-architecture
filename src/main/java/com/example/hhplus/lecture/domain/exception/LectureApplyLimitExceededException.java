package com.example.hhplus.lecture.domain.exception;

import com.example.hhplus.exception.CustomException;

public class LectureApplyLimitExceededException extends CustomException {
	public LectureApplyLimitExceededException() {
		super(400, "신청 가능한 특강 인원이 초과되었습니다.");
	}
}
