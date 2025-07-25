package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;

import java.util.Map;

import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.video.VideoDTO;

public interface AdminCourseService {
    PageResponseDTO<AdminCourseListDisplayDTO> getAdminCourses(Map<String, Object> params);
    AdminCourseDetailDTO getAdminCourseDetail(String courseSeq);
    void updateCourseVisibility(String courseSeq, String isPublic);
    VideoDTO getVideoBySeq(String videoSeq);
    QuizListDTO getQuizListBySeq(String quizListSeq);
}