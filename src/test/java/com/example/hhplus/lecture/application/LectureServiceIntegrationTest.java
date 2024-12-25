package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import com.example.hhplus.lecture.application.exception.LectureNotFoundException;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecturehistory.domain.LectureHistoryRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class LectureServiceIntegrationTest {

	@Autowired
	private LectureService lectureService;

	@Autowired
	private LectureRepository lectureRepository;

	@Autowired
	private LectureHistoryRepository lectureHistoryRepository;

	@Autowired
	private EntityManager entityManager;

	Lecture lecture = Lecture.builder()
			.id(1L)
			.name("강의명1")
			.professor("교수1")
			.startTime(Instant.now().plusSeconds(3600))
			.endTime(Instant.now().plusSeconds(7200))
			.maxApplyCount(30)
			.build();


	@Nested
	class 특강_신청 {

		@Test
		void 성공() {
			// given
			long userId = 2L;
			lectureRepository.upsert(lecture);
			lectureService.apply(new LectureApplyDto(lecture.getId(), userId));
			entityManager.flush();

			List<LectureHistory> userHistories = lectureHistoryRepository.findByUserId(userId);
			assertThat(userHistories.stream().anyMatch(it -> it.getLectureId() == lecture.getId())).isTrue();
		}

		@Test
		void 신청한_특강이_존재하지_않으면_실패한다() {
			// given
			long lectureId = 1L;
			LectureApplyDto undefinedLecture = new LectureApplyDto(lectureId, 2L);

			// when & then
			assertThatThrownBy(() -> lectureService.apply(undefinedLecture))
					.isInstanceOf(LectureNotFoundException.class);
		}

	}

}
