package com.example.hhplus.lecture.infrastructure;

import com.example.hhplus.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

	List<Lecture> findByDate(String date);
}
