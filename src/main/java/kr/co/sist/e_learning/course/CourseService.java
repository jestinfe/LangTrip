package kr.co.sist.e_learning.course;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

public interface CourseService {
	
	public int addCourse(CourseDTO cDTO);
	public List<CourseDTO> selectCourse(Long userSeq);
	public CourseDTO selectCourseData(String courseSeq);
	public List<CourseDTO> selectCourseByPage(Map<String, Object> param);
	public int selectCourseCount(Long userSeq);
	public int modifyCourse(CourseDTO cDTO);
	public int removeCourse(String courseSeq);
}
