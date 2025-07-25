package kr.co.sist.e_learning.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseMapper cm;

	@Override
	public int addCourse(CourseDTO cDTO) {
		int result = cm.insertCourse(cDTO);

		return result;
	}

	@Override
	public List<CourseDTO> selectCourse(Long userSeq) {

		return cm.searchCourseById(userSeq);
	}

	@Override
	public CourseDTO selectCourseData(String courseSeq) {
		return cm.searchCourseByCourseId(courseSeq);
	}

	@Override
	public List<CourseDTO> selectCourseByPage(Map<String, Object> param) {

		return cm.searchCourseByPage(param);
	}

	@Override
	public int selectCourseCount(Long userSeq) {
		return cm.searchCourseCount(userSeq);
	}

	@Override
	public int modifyCourse(CourseDTO cDTO) {
		int result = 0;
		result = cm.updateCourseByCourseSeq(cDTO);
		return result;
	}

	@Override
	public int removeCourse(String courseSeq) {

		return cm.deleteCourseByCourseSeq(courseSeq);
	}

	@Override
	public CourseDTO selectUserSeqByCourseSeq(String courseSeq) {

		return cm.searchCourseByCourseId(courseSeq);
	}

	@Override
	public int updateVideoCount(String courseSeq) {
		int result = 0;
		result = cm.updateVideoCount(courseSeq);
		return result;
	}

	@Override
	public int updateQuizCount(String courseSeq) {
		int result = 0;
		result = cm.updateQuizCount(courseSeq);
		return result;
	}

	@Override
	public void insertCourseStat(CourseStatDTO csDTO) {
		cm.insertCourseStat(csDTO);
	}

	@Override
	public void updateCourseStat(CourseStatDTO csDTO) {
		cm.updateCourseStat(csDTO);
	}

	@Override
	public CourseStatDTO selectCourseStat(Map<String, Object> map) {
		CourseStatDTO csDTO = cm.selectCourseStat(map);
		return csDTO;
	}

	@Override
	public int existCourseStat(Map<String, Object> map) {
		int result = cm.existCourseStat(map);
		return result;
	}

	@Override
	public void minusVideoCount(String courseSeq) {
		
		 cm.minusVideoCount(courseSeq);
	}

	@Override
	public void minusQuizCount(String courseSeq) {
		
		cm.minusQuizCount(courseSeq);
	}

	@Override
	public void plusEnrollCount(String courseSeq) {
		cm.plusEnrollCount(courseSeq);
	}

	@Override
	public void minusEnrollCount(String courseSeq) {

		cm.minusEnrollCount(courseSeq);
	}

}
