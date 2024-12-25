package com.example.hhplus.lecture.application;

import com.example.hhplus.lecture.application.dto.LectureApplyDto;
import com.example.hhplus.lecture.application.exception.LectureNotFoundException;
import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecturehistory.domain.LectureHistoryRepository;
import com.example.hhplus.lecturehistory.domain.LectureHistory;
import com.example.hhplus.lecture.domain.LectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

	@InjectMocks
	private LectureService lectureService;

	@Mock
	private LectureRepository lectureRepository;

	@Mock
	private LectureHistoryRepository lectureHistoryRepository;


	@Nested
	class 특강_신청 {

		@Test
		void 성공() {
			// given
			long lectureId = 1L;
			long userId = 2L;
			Lecture lecture = spy(Lecture.builder().id(lectureId).build());
			LectureHistory lectureHistory = LectureHistory.of(lectureId, userId);

			doNothing().when(lecture).validateApply(anyLong(), anyList());
			when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
			when(lectureHistoryRepository.findByLectureId(lectureId)).thenReturn(List.of());
			when(lectureHistoryRepository.upsert(any(LectureHistory.class))).thenReturn(lectureHistory);

			// when & then
			lectureService.apply(new LectureApplyDto(lectureId, userId));
		}

		@Test
		void 신청한_특강이_존재하지_않으면_실패한다() {
			// given
			long lectureId = 1L;
			LectureApplyDto undefinedLecture = new LectureApplyDto(lectureId, 2L);
			when(lectureRepository.findById(lectureId)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> lectureService.apply(undefinedLecture))
					.isInstanceOf(LectureNotFoundException.class);
		}
	}

}