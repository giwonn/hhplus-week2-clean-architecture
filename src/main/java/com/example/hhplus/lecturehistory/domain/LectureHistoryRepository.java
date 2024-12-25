package com.example.hhplus.lecturehistory.domain;

import java.util.List;

public interface LectureHistoryRepository {
	List<LectureHistory> findByLectureId(long lectureId);

	LectureHistory upsert(LectureHistory lectureHistory);

}
