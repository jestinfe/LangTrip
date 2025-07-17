package kr.co.sist.e_learning.admin.course;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCourseDTO {
    private String courseSeq;
    private String title;
    private String introduction;
    private String instructorId;
    private String instructorNickname;
    private Date uploadDate;
    private String thumbnailPath;
    private String thumbnailName;
    private String isPublic;
    private String category;
    private String status;
}
