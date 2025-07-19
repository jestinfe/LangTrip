package kr.co.sist.e_learning.course;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {

    List<UserCourseListDTO> selectUserCourses(Map<String, Object> param);
    int countUserCourses(Map<String, Object> param);
}