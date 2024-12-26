package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.AvailableLectureDto;
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

	public List<Lecture> findUpcomingLecturesByUserId(AvailableLectureDto dto) {
		List<Lecture> lectures = lectureRepository.findUpcomingListByDate(dto.date());
		List<Long> appliedLectureIds = lectureHistoryRepository.findByUserId(dto.userId()).stream().map(LectureHistory::getLectureId).toList();

		return lectures.stream().filter(
				lecture -> appliedLectureIds.stream()
						.noneMatch(appliedLectureId -> appliedLectureId == lecture.getId())
		).toList();
	}

	public List<Lecture> findAppliedLectures(long userId) {
		List<LectureHistory> userAppliedLectures = lectureHistoryRepository.findByUserId(userId);
		return lectureRepository.findByIds(userAppliedLectures.stream().map(LectureHistory::getLectureId).toList());
	}

	private List<Long> findAppliedUserIds(long lectureId) {
		return lectureHistoryRepository.findByLectureId(lectureId).stream().map(LectureHistory::getLectureId).toList();
	}

}
