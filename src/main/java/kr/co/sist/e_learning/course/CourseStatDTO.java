package kr.co.sist.e_learning.course;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("CourseStatDTO")
public class CourseStatDTO {

	private String courseSeq;
	private Long userSeq;
	private int videoSeq;
	private double courseRate;
	private double courseTime;
	private Date updateTime;
	private int lastTime;
}
