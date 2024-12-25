package com.example.hhplus.lecturehistory.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class LectureHistoryId implements Serializable {

	private long lectureId;

	private long userId;

}
