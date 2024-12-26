package com.example.hhplus.lecture.domain;


import com.example.hhplus.lecture.domain.exception.LectureAlreadyAppliedExceededException;
import com.example.hhplus.lecture.domain.exception.LectureApplyDeadlineExceededException;
import com.example.hhplus.lecture.domain.exception.LectureApplyLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class LectureTest {

	@Test
	void 특강_시작_전에_특강을_신청하면_성공한다() {
		// given
		long lectureId = 1L;
		Instant lectureStartTime = Instant.parse("2024-01-01T02:00:00Z");
		Lecture lecture = Lecture.builder().id(lectureId).startTime(lectureStartTime).build();
		Instant applyTime = lectureStartTime.minus(1, ChronoUnit.MINUTES);

		// when & then
		assertThatCode(() -> lecture.validateApplyDeadLine(applyTime))
				.doesNotThrowAnyException();
	}

	@Test
	void 특강_시작_시간부터_특강을_신청하면_실패한다() {
		// given
		long lectureId = 1L;
		Instant lectureStartTime = Instant.parse("2024-01-01T02:00:00Z");
		Lecture lecture = Lecture.builder().id(lectureId).startTime(lectureStartTime).build();

		// when & then
		assertThatThrownBy(() -> lecture.validateApplyDeadLine(lectureStartTime))
				.isInstanceOf(LectureApplyDeadlineExceededException.class);
	}

	@Test
	void 이미_신청한_특강이면_신청에_실패한다() {
		// given
		long userId = 1L;
		List<Long> applicants = List.of(1L);
		Lecture lecture = Lecture.builder().build();

		// when & then
		assertThatThrownBy(() -> lecture.validateAlreadyApplied(userId, applicants))
				.isInstanceOf(LectureAlreadyAppliedExceededException.class);
	}

	@Test
	void 특강_정원이_초과하면_신청할_수_없다() {
		// given
		List<Long> applicants = List.of(2L);
		Lecture lecture = Lecture.builder().maxApplyCount(1).build();

		// when & then
		assertThatThrownBy(() -> lecture.validateApplyLimit(applicants.size()))
				.isInstanceOf(LectureApplyLimitExceededException.class);
	}

}

