package kr.co.sist.e_learning.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;

import java.util.Map;

public interface CourseService {
    PageResponseDTO<UserCourseListDTO> getAllCourses(Map<String, Object> params);
}
