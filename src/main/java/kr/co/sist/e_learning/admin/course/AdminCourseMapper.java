package kr.co.sist.e_learning.admin.course;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminCourseMapper {

    List<AdminCourseDTO> selectCourses(Map<String, Object> param);
    int countCourses(Map<String, Object> param);
    AdminCourseDTO selectCourseDetail(String courseSeq);
    int updateCourseVisibility(Map<String, Object> param);
    int updateCourseStatus(Map<String, Object> param);
}
