package kr.co.sist.e_learning.admin.course;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminCourseMapper {

    List<AdminCourseDTO> selectAllCourses();

    int updateCourseVisibility(Map<String, Object> param);
}
