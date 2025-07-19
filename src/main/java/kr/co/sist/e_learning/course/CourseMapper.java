package kr.co.sist.e_learning.course;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper {
    List<UserCourseListDTO> selectAllCourses(Map<String, Object> params);
    int countAllCourses(Map<String, Object> params);
}
