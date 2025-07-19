package kr.co.sist.e_learning.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    @Override
    public PageResponseDTO<UserCourseListDTO> getUserCourses(PageRequestDTO pageRequestDTO, String searchTerm, String category, String difficulty, String priceRange) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", pageRequestDTO.getOffset());
        param.put("limit", pageRequestDTO.getLimit());
        param.put("searchTerm", searchTerm);
        param.put("category", category);
        param.put("difficulty", difficulty);
        param.put("priceRange", priceRange);
        param.put("orderBy", pageRequestDTO.getOrderBy());
        param.put("sort", pageRequestDTO.getSort());

        List<UserCourseListDTO> courses = courseMapper.selectUserCourses(param);
        int totalCount = courseMapper.countUserCourses(param);

        return new PageResponseDTO<>(courses, totalCount, pageRequestDTO.getPage(), pageRequestDTO.getLimit(), 5);
    }
}