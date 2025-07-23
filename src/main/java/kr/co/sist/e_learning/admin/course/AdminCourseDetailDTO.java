package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.video.VideoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class AdminCourseDetailDTO {
    private String courseSeq;
    private String userSeq; // Creator's userSeq
    private String title;
    private String introduction;
    private LocalDateTime uploadDate;
    private String isPublic; // Y/N
    private String category;
    private String difficulty;
    private String thumbnailPath;
    private String thumbnailName;
    private String userName; // Creator's name
    private List<VideoDTO> videos;
    private List<QuizListDTO> quizzes;
}
