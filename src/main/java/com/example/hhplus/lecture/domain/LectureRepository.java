package com.example.hhplus.lecture.domain;

import java.util.List;
import java.util.Optional;

public interface LectureRepository {

	Optional<Lecture> findById(long id);

	List<Lecture> findUpcomingListByDate(String date);
	Lecture upsert(Lecture lecture);
}
