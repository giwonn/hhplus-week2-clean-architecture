package com.example.hhplus.lecture.domain;
import java.util.Optional;

public interface LectureRepository {

	Optional<Lecture> findById(long id);
}
