package com.example.hhplus.lecture.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record AppliedLectureRequest(
		@NotNull
		long userId
) {
}
