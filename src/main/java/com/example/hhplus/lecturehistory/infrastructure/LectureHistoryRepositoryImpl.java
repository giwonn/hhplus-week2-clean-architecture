package com.example.hhplus.lecturehistory.infrastructure;

import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecturehistory.domain.LectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LectureHistoryRepositoryImpl implements LectureHistoryRepository {

	private final LectureHistoryJpaRepository lectureHistoryJpaRepository;

	@Override
	public List<LectureHistory> findByUserId(long id) {
		return lectureHistoryJpaRepository.findByIdUserId(id);
	}

	@Override
	public List<LectureHistory> findByLectureId(long id) {
		return lectureHistoryJpaRepository.findByIdLectureId(id);
	}

	@Override
	public LectureHistory upsert(LectureHistory lectureHistory) {
		return lectureHistoryJpaRepository.save(lectureHistory);
	}


}
