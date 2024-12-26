package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.AvailableLectureDto;
import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import com.example.hhplus.lecture.application.exception.LectureNotFoundException;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecturehistory.domain.LectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepository;

	private final LectureHistoryRepository lectureHistoryRepository;

	@Transactional
	public Lecture apply(LectureApplyDto dto) {
		Lecture lecture = lectureRepository.findByIdForUpdate(dto.lectureId()).orElseThrow(LectureNotFoundException::new);
		List<Long> userIds = findAppliedUserIds(dto.lectureId());
		lecture.validateApply(dto.userId(), userIds);
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
		List<LectureHistory> test = lectureHistoryRepository.findByLectureId(lectureId);
		return test.stream().map(LectureHistory::getUserId).toList();
	}

}
