package kr.co.sist.e_learning.course;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserCourseListDTO {
    private String courseSeq;
    private String title;
    private String introduction;
    private LocalDateTime uploadDate;
    private String isPublic;
    private String userName; // Assuming a course has an instructor/creator
}