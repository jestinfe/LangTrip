package kr.co.sist.e_learning.course;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("CourseNoticeDTO")

public class CourseNoticeDTO {
	private int noticeSeq;
    private int courseSeq;
    private String content;
    private Date regDate;
}
