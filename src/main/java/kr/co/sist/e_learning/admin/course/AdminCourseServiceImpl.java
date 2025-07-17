package kr.co.sist.e_learning.admin.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.common.aop.Loggable;
import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCourseServiceImpl implements AdminCourseService {

    private final AdminCourseMapper courseMapper;

    @Override
    public PageResponseDTO<AdminCourseDTO> getCourses(PageRequestDTO pageRequestDTO, String title, String category, boolean includeInactive) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", pageRequestDTO.getOffset());
        param.put("limit", pageRequestDTO.getLimit());
        param.put("title", title);
        param.put("category", category);
        param.put("includeInactive", includeInactive);
        param.put("orderBy", pageRequestDTO.getOrderBy());
        param.put("sort", pageRequestDTO.getSort());

        List<AdminCourseDTO> courses = courseMapper.selectCourses(param);
        int totalCount = courseMapper.countCourses(param);

        return new PageResponseDTO<>(courses, totalCount, pageRequestDTO.getPage(), pageRequestDTO.getLimit(), 5);
    }

    @Override
    public AdminCourseDTO getCourseDetail(String courseSeq) {
        return courseMapper.selectCourseDetail(courseSeq);
    }

    @Override
    @Loggable(actionType = "COURSE_VISIBILITY_UPDATE")
    public void updateCourseVisibility(String courseSeq, String isPublic) {
        Map<String, Object> param = new HashMap<>();
        param.put("courseSeq", courseSeq);
        param.put("isPublic", isPublic);
        courseMapper.updateCourseVisibility(param);
    }

    @Override
    @Loggable(actionType = "COURSE_SOFT_DELETE")
    public void softDeleteCourse(String courseSeq) {
        Map<String, Object> param = new HashMap<>();
        param.put("courseSeq", courseSeq);
        param.put("status", "DELETED"); // Set status to DELETED for soft delete
        courseMapper.updateCourseStatus(param);
    }
}
