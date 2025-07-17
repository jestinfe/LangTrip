package kr.co.sist.e_learning.course;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

public interface CourseService {
    PageResponseDTO<UserCourseListDTO> getUserCourses(PageRequestDTO pageRequestDTO, String searchTerm, String category, String difficulty, String priceRange);
}