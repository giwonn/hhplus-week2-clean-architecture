package com.example.hhplus.lecture.presentation.dto;

import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import jakarta.validation.constraints.NotNull;

public record LectureApplyRequest(
		@NotNull
		long lectureId,

		@NotNull
		long userId
) {
	public LectureApplyDto toDto() {
		return new LectureApplyDto(
				lectureId,
				userId
		);
	}
}
