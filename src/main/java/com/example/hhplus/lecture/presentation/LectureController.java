package com.example.hhplus.lecture.presentation;

import com.example.hhplus.lecture.application.LectureService;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.presentation.dto.LectureApplyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

	public final LectureService lectureService;

	@PostMapping("/apply")
	public Lecture apply(@RequestBody LectureApplyRequest req) {
		return lectureService.apply(req.toDto());
	}

}
