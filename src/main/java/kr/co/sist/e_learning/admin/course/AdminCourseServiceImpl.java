package kr.co.sist.e_learning.admin.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.common.aop.Loggable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCourseServiceImpl implements AdminCourseService {

    private final AdminCourseMapper courseMapper;

    @Override
    public List<AdminCourseDTO> getAllCourses() {
        return courseMapper.selectAllCourses();
    }

    @Override
    @Loggable(actionType = "COURSE_VISIBILITY_UPDATE")
    public void updateCourseVisibility(String courseSeq, String isPublic) {
        Map<String, Object> param = new HashMap<>();
        param.put("courseSeq", courseSeq);
        param.put("isPublic", isPublic);
        courseMapper.updateCourseVisibility(param);
    }
}
