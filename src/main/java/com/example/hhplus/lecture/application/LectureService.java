package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import com.example.hhplus.lecture.application.exception.LectureNotFoundException;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecturehistory.domain.LectureHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepository;

	private final LectureHistoryRepository lectureHistoryRepository;

	@Transactional
	public Lecture apply(LectureApplyDto dto) {
		Lecture lecture = lectureRepository.findById(dto.lectureId()).orElseThrow(LectureNotFoundException::new);
		lecture.validateApply(dto.userId(), findAppliedUserIds(dto.lectureId()));
		lectureHistoryRepository.upsert(LectureHistory.of(dto.lectureId(), dto.userId()));

		return lecture;
	}

	private List<Long> findAppliedUserIds(long lectureId) {
		return lectureHistoryRepository.findByLectureId(lectureId).stream().map(LectureHistory::getLectureId).toList();
	}

}
