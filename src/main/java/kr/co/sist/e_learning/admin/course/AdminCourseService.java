package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;

import java.util.Map;

public interface AdminCourseService {
    PageResponseDTO<AdminCourseListDisplayDTO> getAdminCourses(Map<String, Object> params);
    AdminCourseDetailDTO getAdminCourseDetail(String courseSeq);
    void updateCourseVisibility(String courseSeq, String isPublic);
}