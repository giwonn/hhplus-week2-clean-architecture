package com.example.hhplus.lecture.domain;

import java.util.List;
import java.util.Optional;

public interface LectureRepository {

	Optional<Lecture> findById(long id);

	Optional<Lecture> findByIdForUpdate(long id);

	List<Lecture> findUpcomingListByDate(String date);

	List<Lecture> findByIds(List<Long> ids);

	Lecture upsert(Lecture lecture);
}
