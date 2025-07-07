package kr.co.sist.e_learning.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureHistoryDTO {
    private String videoTitle;
    private String courseTitle;
    private String instructorName;
    private String uploadDate;
    private String completedYn;
}
