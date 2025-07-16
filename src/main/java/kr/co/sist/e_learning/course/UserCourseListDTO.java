package kr.co.sist.e_learning.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCourseListDTO {
    private String courseSeq;
    private String title;
    private String category;
    private String instructorNickname;
    private double rating;
    private int students;
    private int originalPrice;
    private int currentPrice;
    private String difficulty;
    private String duration; // This might need to be calculated or stored differently
    private String thumbnailName;
    private String thumbnailPath;
}
