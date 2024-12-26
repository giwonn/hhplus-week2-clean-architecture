package com.example.hhplus.lecture.application.dto;

public record AvailableLectureDto(
		long userId,
		String date
) {
	public AvailableLectureDto toDto() {
		return new AvailableLectureDto(
				userId,
				date
		);
	}
}
