package com.example.hhplus.lecture.presentation.dto;

import com.example.hhplus.lecture.domain.Lecture;

import java.util.List;

public record AvailableLectureResponse(
		List<Lecture> lectures
) {
	public static AvailableLectureResponse from(List<Lecture> lectures) {
		return new AvailableLectureResponse(lectures);
	}
}
