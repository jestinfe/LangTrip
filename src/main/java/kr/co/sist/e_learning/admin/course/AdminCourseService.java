package kr.co.sist.e_learning.admin.course;

import java.util.List;

public interface AdminCourseService {

    List<AdminCourseDTO> getAllCourses();

    void updateCourseVisibility(String courseSeq, String isPublic);
}
