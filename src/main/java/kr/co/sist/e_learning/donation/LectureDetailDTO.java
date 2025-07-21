package kr.co.sist.e_learning.donation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureDetailDTO {
    private Long lectureId;        // course_seq
    private String title;          // 강의 제목
    private Long instructorId;     // user_seq
    private String instructorName; // users.nickname
}
