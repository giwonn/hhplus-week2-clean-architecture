package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.AvailableLectureDto;
import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import com.example.hhplus.lecture.application.exception.LectureNotFoundException;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
import com.example.hhplus.lecture.domain.exception.LectureAlreadyAppliedExceededException;
import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecturehistory.domain.LectureHistoryRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
class LectureServiceIntegrationTest {

	@Autowired
	private LectureService lectureService;

	@Autowired
	private LectureRepository lectureRepository;

	@Autowired
	private LectureHistoryRepository lectureHistoryRepository;


	@Nested
	class 특강_신청 {

		@Test
		void 성공() {
			// given
			Lecture lecture = Lecture.builder()
					.id(1L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			long userId = 2L;
			lectureRepository.upsert(lecture);
			lectureService.apply(new LectureApplyDto(lecture.getId(), userId));

			List<LectureHistory> userHistories = lectureHistoryRepository.findByUserId(userId);
			assertThat(userHistories.stream().anyMatch(it -> it.getLectureId() == lecture.getId())).isTrue();
		}

		@Test
		void 신청한_특강이_존재하지_않으면_실패한다() {
			// given
			Lecture lecture = Lecture.builder()
					.id(3L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			LectureApplyDto undefinedLecture = new LectureApplyDto(lecture.getId(), 4L);

			// when & then
			assertThatThrownBy(() -> lectureService.apply(undefinedLecture))
					.isInstanceOf(LectureNotFoundException.class);
		}

		@Test
		void 특강을_중복_신청하면_예외가_발생한다() {
			// given
			Lecture lecture = Lecture.builder()
					.id(5L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			lectureRepository.upsert(lecture);
			lectureService.apply(new LectureApplyDto(lecture.getId(), 6L));

			// when & then
			assertThatThrownBy(() -> lectureService.apply(new LectureApplyDto(lecture.getId(), 6L)))
					.isInstanceOf(LectureAlreadyAppliedExceededException.class);
		}
	}

	@Nested
	class 신청_가능_특강_조회 {

		@Test
		void 성공() {
			// given
			Lecture lecture = Lecture.builder()
					.id(7L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			lectureRepository.upsert(lecture);
			long userId = 8L;
			AvailableLectureDto dto = new AvailableLectureDto(userId, "2024-01-01");

			// when
			List<Lecture> availableLectures = lectureService.findUpcomingLecturesByUserId(dto);

			// then
			assertThat(availableLectures.get(0).getId()).isEqualTo(lecture.getId());
		}
	}

	@Nested
	class 신청_완료_특강_조회 {

		@Test
		void 성공() {
			// given
			Lecture lecture = Lecture.builder()
					.id(9L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			long userId = 10L;
			lectureRepository.upsert(lecture);
			lectureService.apply(new LectureApplyDto(lecture.getId(), userId));

			// when
			List<Lecture> appliedLectures = lectureService.findAppliedLectures(userId);

			// then
			assertThat(appliedLectures).hasSize(1);
			assertThat(appliedLectures.get(0).getId()).isEqualTo(lecture.getId());
		}
	}

}
