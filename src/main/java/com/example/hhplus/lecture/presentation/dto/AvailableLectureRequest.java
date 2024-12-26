package com.example.hhplus.lecture.presentation.dto;

import com.example.hhplus.lecture.application.dto.AvailableLectureDto;
import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AvailableLectureRequest(
		@NotNull
		long userId,

		@NotNull
		@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
		String date
) {
	public AvailableLectureDto toDto() {
		return new AvailableLectureDto(
				userId,
				date
		);
	}
}
