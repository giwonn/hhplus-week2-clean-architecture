package com.example.hhplus.lecture.presentation;

import com.example.hhplus.lecture.domain.Lecture;
import com.example.hhplus.lecture.domain.LectureRepository;
import com.example.hhplus.lecture.presentation.dto.LectureApplyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LectureControllerE2ETest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private LectureRepository lectureRepository;

	Lecture lecture = Lecture.builder()
			.id(1L)
			.name("강의명1")
			.professor("교수1")
			.startTime(Instant.now().plusSeconds(3600))
			.endTime(Instant.now().plusSeconds(7200))
			.maxApplyCount(30)
			.build();

	@Test
	void 특강_신청_성공() {
		// given
		lectureRepository.upsert(lecture);
		LectureApplyRequest request = new LectureApplyRequest(lecture.getId(), 2L);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<LectureApplyRequest> httpEntity = new HttpEntity<>(request, headers);

		// when
		ResponseEntity<Lecture> response = restTemplate.exchange(
				"/lecture/apply",
				HttpMethod.POST,
				httpEntity,
				Lecture.class
		);

		// then
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		Lecture responseBody = response.getBody();
		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getId()).isEqualTo(lecture.getId());
	}

}
