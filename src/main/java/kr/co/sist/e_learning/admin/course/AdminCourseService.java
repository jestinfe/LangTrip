package kr.co.sist.e_learning.admin.course;

import java.util.List;
import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

public interface AdminCourseService {

    PageResponseDTO<AdminCourseDTO> getCourses(PageRequestDTO pageRequestDTO, String title, String category, boolean includeInactive);
    AdminCourseDTO getCourseDetail(String courseSeq);
    void updateCourseVisibility(String courseSeq, String isPublic);
    void softDeleteCourse(String courseSeq);
}
