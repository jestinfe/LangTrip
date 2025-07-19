package kr.co.sist.e_learning.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageResponseDTO<UserCourseListDTO> getAllCourses(Map<String, Object> params) {
        int page = (int) params.get("page");
        int pageSize = (int) params.get("pageSize");

        List<UserCourseListDTO> list = courseMapper.selectAllCourses(params);
        int totalCount = courseMapper.countAllCourses(params);

        return new PageResponseDTO<>(list, totalCount, page, pageSize, 5);
    }
}
