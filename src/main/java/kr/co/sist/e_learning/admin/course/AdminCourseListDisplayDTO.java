package kr.co.sist.e_learning.admin.course;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AdminCourseListDisplayDTO {
    private String courseSeq;
    private String title;
    private String category;
    private String difficulty;
    private LocalDateTime uploadDate;
    private String isPublic;
    private String userName; // Instructor's name
    private int videoCount;
    private int quizCount;
}
