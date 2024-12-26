package com.example.hhplus.lecturehistory.domain;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Entity
public class LectureHistory implements Serializable {

	@EmbeddedId
	private LectureHistoryId id;

	private LectureHistory(LectureHistoryId id) {
		this.id = id;
	}

	public static LectureHistory of(long lectureId, long userId) {
		return new LectureHistory(new LectureHistoryId(lectureId, userId));
	}

	public long getLectureId() {
		return id.getLectureId();
	}

	public long getUserId() {
		return id.getUserId();
	}
}
