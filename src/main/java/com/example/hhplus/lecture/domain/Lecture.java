package com.example.hhplus.lecture.domain;

import com.example.hhplus.lecture.domain.exception.LectureAlreadyAppliedExceededException;
import com.example.hhplus.lecture.domain.exception.LectureApplyDeadlineExceededException;
import com.example.hhplus.lecture.domain.exception.LectureApplyLimitExceededException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Lecture {

	@Id
	private long id;

	@NotNull
	private String name;

	@NotNull
	private String professor;

	@NotNull
	private Instant startTime;

	@NotNull
	private Instant endTime;

	@NotNull
	private int maxApplyCount;

	@Builder
	protected Lecture(long id, String name, String professor, Instant startTime, Instant endTime, int maxApplyCount) {
		this.id = id;
		this.name = name;
		this.professor = professor;
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxApplyCount = maxApplyCount;
	}

	private Lecture(String name, String professor, Instant startTime, Instant endTime, int maxApplyCount) {
		this.name = name;
		this.professor = professor;
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxApplyCount = maxApplyCount;
	}

	public static Lecture of(String name, String professer, Instant startTime, Instant endTime) {
		return new Lecture(name, professer, startTime, endTime, 30);
	}

	public void validateApply(long userId, List<Long> currentApplicants) {
		validateApplyDeadLine(Instant.now());
		validateAlreadyApplied(userId, currentApplicants);
		validateApplyLimit(currentApplicants.size());
	}

	void validateApplyDeadLine(Instant time) {
		if (startTime == time || time.isAfter(startTime)) {
			throw new LectureApplyDeadlineExceededException();
		}
	}

	void validateAlreadyApplied(long userId, List<Long> applicants) {
		if (applicants.stream().anyMatch(applicant -> applicant == userId)) {
			throw new LectureAlreadyAppliedExceededException();
		}
	}

	void validateApplyLimit(int currentApplicants) {
		if (currentApplicants >= maxApplyCount) {
			throw new LectureApplyLimitExceededException();
		}
	}

}
