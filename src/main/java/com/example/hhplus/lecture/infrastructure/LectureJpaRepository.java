package com.example.hhplus.lecture.infrastructure;

import com.example.hhplus.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

	List<Lecture> findByDate(String date);

	@Query("SELECT lecture FROM Lecture lecture WHERE lecture.id IN :ids")
	List<Lecture> findByIds(@Param("ids") List<Long> ids);

}
