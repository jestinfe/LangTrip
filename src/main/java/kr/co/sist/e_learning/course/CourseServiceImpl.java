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
	
		
		return  cm.deleteCourseByCourseSeq(courseSeq);
	}
	
}
