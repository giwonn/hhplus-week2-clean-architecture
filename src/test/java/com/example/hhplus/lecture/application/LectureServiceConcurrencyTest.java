package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
class LectureServiceConcurrencyTest {

	@Autowired
	private LectureService lectureService;

	@Autowired
	LectureRepository lectureRepository;

	@Autowired
	LectureHistoryRepository lectureHistoryRepository;

	@Nested
	class 특강신청_40명중에_30명까지만_신청에_성공한다 {

		@Test
		void 성공() throws Exception {
			// given
			Lecture lecture = Lecture.builder()
					.id(9999L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			lectureRepository.upsert(lecture);

			int tryCount = 40;
			List<Callable<Void>> tasks = new ArrayList<>();
			for (long i = 1; i <= tryCount; i++) {
				LectureApplyDto lectureDto = new LectureApplyDto(lecture.getId(), 9000+i);
				tasks.add(() -> {
					lectureService.apply(lectureDto);
					return null;
				});
			}
			ExecutorService executorService = Executors.newFixedThreadPool(tryCount);

			AtomicInteger successCount = new AtomicInteger();
			AtomicInteger failCount = new AtomicInteger();

			// when
			List<Future<Void>> futures = executorService.invokeAll(tasks);
			for (Future<Void> future : futures) {
				try {
					future.get();
					successCount.incrementAndGet();
				} catch (Exception e) {
					failCount.incrementAndGet();
				}
			}
			executorService.shutdown();

			// then
			List<LectureHistory> lectureHistories = lectureHistoryRepository.findByLectureId(lecture.getId());
			assertSoftly(softly -> {
				softly.assertThat(lectureHistories).hasSize(30);
				softly.assertThat(successCount.get()).isEqualTo(30);
				softly.assertThat(failCount.get()).isEqualTo(10);
			});


		}

	}

	@Nested
	class 동일_유저로_같은_특강을_신청할_때_1번만_성공한다 {

		@Test
		void 성공() throws Exception {
			// given
			Lecture lecture = Lecture.builder()
					.id(9998L)
					.name("강의명1")
					.professor("교수1")
					.date("2024-01-01")
					.startTime(Instant.now().plusSeconds(3600))
					.endTime(Instant.now().plusSeconds(7200))
					.maxApplyCount(30)
					.build();
			long userId = 8999L;
			lectureRepository.upsert(lecture);

			int tryCount = 5;
			List<Callable<Void>> tasks = new ArrayList<>();
			for (int i = 1; i <= tryCount; i++) {
				LectureApplyDto lectureDto = new LectureApplyDto(lecture.getId(), userId);
				tasks.add(() -> {
					lectureService.apply(lectureDto);
					return null;
				});
			}
			ExecutorService executorService = Executors.newFixedThreadPool(tryCount);

			AtomicInteger successCount = new AtomicInteger();
			AtomicInteger failCount = new AtomicInteger();


			// when
			List<Future<Void>> futures = executorService.invokeAll(tasks);
			for (Future<Void> future : futures) {
				try {
					future.get();
					successCount.incrementAndGet();
				} catch (Exception e) {
					failCount.incrementAndGet();
				}
			}
			executorService.shutdown();

			// then
			List<LectureHistory> lectureHistories = lectureHistoryRepository.findByLectureId(lecture.getId());
			assertSoftly(softly -> {
				softly.assertThat(lectureHistories).hasSize(1);
				softly.assertThat(lectureHistories.get(0).getLectureId()).isEqualTo(lecture.getId());
				softly.assertThat(lectureHistories.get(0).getUserId()).isEqualTo(userId);
				softly.assertThat(successCount.get()).isEqualTo(1);
				softly.assertThat(failCount.get()).isEqualTo(4);
			});

		}

	}
}
