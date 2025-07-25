package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.quiz.QuizMapper;
import kr.co.sist.e_learning.video.VideoDTO;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private AdminCourseMapper adminCourseMapper;
    @Autowired
    private QuizMapper quizMapper;

    @Override
    public PageResponseDTO<AdminCourseListDisplayDTO> getAdminCourses(Map<String, Object> params) {
        int page = (int) params.get("page");
        int pageSize = (int) params.get("pageSize");

        List<AdminCourseDTO> rawList = adminCourseMapper.selectAdminCourses(params);
        List<AdminCourseListDisplayDTO> displayList = rawList.stream().map(raw -> {
            AdminCourseListDisplayDTO displayDTO = new AdminCourseListDisplayDTO();
            BeanUtils.copyProperties(raw, displayDTO);
            return displayDTO;
        }).collect(Collectors.toList());

        int totalCount = adminCourseMapper.countAdminCourses(params);

        return new PageResponseDTO<>(displayList, totalCount, page, pageSize, 5);
    }

    @Override
    public AdminCourseDetailDTO getAdminCourseDetail(String courseSeq) {
        return adminCourseMapper.selectAdminCourseDetail(courseSeq);
    }

    @Override
    public void updateCourseVisibility(String courseSeq, String isPublic) {
        adminCourseMapper.updateCourseVisibility(courseSeq, isPublic);
    }

    @Override
    public VideoDTO getVideoBySeq(String videoSeq) {
        return adminCourseMapper.selectVideoBySeq(videoSeq);
    }

    @Override
    public QuizListDTO getQuizListBySeq(String quizListSeq) {
        return quizMapper.selectQuizListBySeq(quizListSeq);
    }
}