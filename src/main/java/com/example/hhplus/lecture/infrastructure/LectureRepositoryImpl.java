package com.example.hhplus.lecture.infrastructure;

import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryImpl implements LectureRepository {

	private final LectureJpaRepository lectureJpaRepository;

	@Override
	public Optional<Lecture> findById(long id) {
		return lectureJpaRepository.findById(id);
	}

	@Override
	public List<Lecture> findUpcomingListByDate(String date) {
		return lectureJpaRepository.findByDate(date);
	}
	@Override
	public Lecture upsert(Lecture lecture) {
		return lectureJpaRepository.save(lecture);
	}
}
