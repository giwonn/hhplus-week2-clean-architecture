package com.example.hhplus.lecturehistory.infrastructure;

import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecturehistory.domain.LectureHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureHistoryJpaRepository extends JpaRepository<LectureHistory, LectureHistoryId> {

	List<LectureHistory> findByIdLectureId(long lectureId);

}
