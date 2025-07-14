package kr.co.sist.e_learning.admin.course;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCourseDTO {
	private String courseSeq;
    private String title;
    private String instructorName;
    private Date uploadDate;
    private String isPublic;
}
