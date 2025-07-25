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

	public CourseDTO selectUserSeqByCourseSeq(String courseSeq);

	public int updateVideoCount(String courseSeq);

	public int updateQuizCount(String courseSeq);

	public void minusVideoCount(String courseSeq);

	public void minusQuizCount(String courseSeq);

	public void plusEnrollCount(String courseSeq);

	public void minusEnrollCount(String courseSeq);

	// 동영상 진행률
	public void insertCourseStat(CourseStatDTO csDTO);

	public void updateCourseStat(CourseStatDTO csDTO);

	public CourseStatDTO selectCourseStat(Map<String, Object> map);

	public int existCourseStat(Map<String, Object> map);
}
