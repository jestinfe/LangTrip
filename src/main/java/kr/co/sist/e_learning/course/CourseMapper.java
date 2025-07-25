package kr.co.sist.e_learning.course;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CourseMapper {
	
	 public int insertCourse(CourseDTO cDTO);
	 public List<CourseDTO> searchCourseById(Long userSeq);
	 public CourseDTO searchCourseByCourseId(String courseSeq);
	 public List<CourseDTO> searchCourseByPage(Map<String, Object> paramMap);
	 public int searchCourseCount(Long userSeq);
	 public int updateCourseByCourseSeq(CourseDTO cDTO);
	 public int deleteCourseByCourseSeq(String courseSeq);
	 public CourseDTO selectUserSeqByCourseSeq(String courseSeq);
	 public int updateVideoCount(String courseSeq);
	 public int updateQuizCount(String courseSeq);
	 public void minusVideoCount(String courseSeq);
	 public void minusQuizCount(String courseSeq);
	 
	 public void plusEnrollCount(String courseSeq);
	 public void minusEnrollCount(String courseSeq);
	 
	 //동영상 5초마다 
	 public void insertCourseStat(CourseStatDTO csDTO);
	 public void updateCourseStat(CourseStatDTO csDTO);
	 public CourseStatDTO selectCourseStat(Map<String, Object> map);
	 public int existCourseStat(Map<String, Object> map);
}
