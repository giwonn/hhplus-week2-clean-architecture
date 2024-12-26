package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.AvailableLectureDto;
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
			.date("2024-01-01")
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

	@Nested
	class 신청_가능_특강_조회 {

		@Test
		void 성공() {
			// given
			lectureRepository.upsert(lecture);
			long userId = 1L;
			AvailableLectureDto dto = new AvailableLectureDto(userId, "2024-01-01");

			// when
			List<Lecture> availableLectures = lectureService.findUpcomingLecturesByUserId(dto);

			// then
			assertThat(availableLectures.get(0).getId()).isEqualTo(1L);
		}

		@Test
		void 신청_가능한_특강이_없으면_빈_리스트_조회() {
			// given
			long userId = 1L;
			lectureRepository.upsert(lecture);
			lectureHistoryRepository.upsert(LectureHistory.of(lecture.getId(), userId));
			AvailableLectureDto dto = new AvailableLectureDto(userId, lecture.getDate());

			// when
			List<Lecture> availableLectures = lectureService.findUpcomingLecturesByUserId(dto);

			// then
			assertThat(availableLectures).isEmpty();
		}
	}

	@Nested
	class 신청_완료_특강_조회 {

		@Test
		void 성공() {
			// given
			long userId = 1L;
			lectureRepository.upsert(lecture);
			lectureHistoryRepository.upsert(LectureHistory.of(lecture.getId(), userId));

			// when
			List<Lecture> appliedLectures = lectureService.findAppliedLectures(userId);

			// then
			assertThat(appliedLectures).hasSize(1);
			assertThat(appliedLectures.get(0).getId()).isEqualTo(lecture.getId());
		}

		@Test
		void 신청_완료한_특강이_없으면_빈_리스트_조회() {
			// given
			long userId = 1L;
			lectureRepository.upsert(lecture);
			lectureHistoryRepository.upsert(LectureHistory.of(lecture.getId(), userId));

			// when
			List<Lecture> appliedLectures = lectureService.findAppliedLectures(userId);

			// then
			assertThat(appliedLectures).hasSize(1);
			assertThat(appliedLectures.get(0).getId()).isEqualTo(lecture.getId());
		}
	}

}
