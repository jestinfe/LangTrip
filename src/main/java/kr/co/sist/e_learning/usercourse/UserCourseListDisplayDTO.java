package kr.co.sist.e_learning.usercourse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserCourseListDisplayDTO {
    private String courseSeq;
    private String title;
    private String introduction;
    private LocalDateTime uploadDate;
    private String category;
    private String difficulty;
    private String thumbnailPath;
    private String thumbnailName;
    private String instructorName; // Assuming this will come from a join with USERS table
    private String userId; // Instructor's user ID
}
