package com.example.hhplus.lecture.presentation;

import com.example.hhplus.lecture.application.LectureService;
import com.example.hhplus.lecture.application.dto.AvailableLectureDto;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.presentation.dto.AvailableLectureResponse;
import com.example.hhplus.lecture.presentation.dto.LectureApplyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

	public final LectureService lectureService;

	@PostMapping("/apply")
	public Lecture apply(@RequestBody LectureApplyRequest req) {
		return lectureService.apply(req.toDto());
	}

	@GetMapping("/available")
	public AvailableLectureResponse availableList(@ModelAttribute @Validated AvailableLectureDto dto) {
		List<Lecture> lectures = lectureService.findUpcomingLecturesByUserId(dto.toDto());
		return AvailableLectureResponse.from(lectures);
	}

}
