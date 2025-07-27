package kr.co.sist.e_learning.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.sist.e_learning.quiz.QuizDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CombineDTO {
//	 private String quizListSeq; //pk
//	 private String courseSeq;   //fk
//	 private Long userSeq;   //fk
//	 private String langCategory;//언어 분류 (영어,중국어,일본어)
//	 private String title;       //퀴즈묶음제목
//	 private Date uploadDate;	//생성일
//	 private String status;       //상태 -> 학습전/학습중(중도포기)/학습완료
//	 private String isDelete;       // 삭제 여부 Y / N
//	 private LocalDateTime modifyDate;       // 퀴즈 목록 수정일 TIMESTAMP DEFAULT NULL
//	 //퀴즈묶음
//	 private List<QuizDTO> quiz = new ArrayList<>();
//	 
//	 
//	  private int videoSeq;     // PK
////	    private String courseSeq;    // 강의 번호 (FK)
//	    private String videoTitle;
//	    private String filePath;     // 서버상의 실제 경로
//	    private String fileName;     // 원본 파일명
////	    private Date uploadDate;
//	    private String description;
//	    private String isCompleted;  // 완료 여부 (Y/N)
//	    private Integer videoOrder;  // 영상 순서
//	    private String type;
	private String type; // "video" or "quiz"
    private int videoSeq; // 비디오의 경우
    private String quizListSeq; // 퀴즈의 경우
    private String courseSeq;
    private Date uploadDate;
    
    public CombineDTO(String type, int videoSeq, String courseSeq, Date uploadDate) {
        this.type = type;
        this.videoSeq = videoSeq;
        this.courseSeq = courseSeq;
        this.uploadDate = uploadDate;
    }
    
    public CombineDTO(String type, String quizListSeq, String courseSeq, java.sql.Date uploadDate) {
        this.type = type;
        this.quizListSeq = quizListSeq;
        this.courseSeq = courseSeq;
        this.uploadDate= new java.util.Date(uploadDate.getTime());
    }
	 
}
