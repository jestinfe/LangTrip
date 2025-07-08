package kr.co.sist.e_learning.quiz;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizDTO {
	  private String quizSeq;
	  private String quizListSeq;
	  private String courseSeq;
	  private String correctOption;
	  private String hint;
	  private String question;
	  private int quizOrder;
	  private Date modifyDate;
	  private Date uploadDate;
	  private boolean isDelete;
	  private List<QuizOptionDTO> option;
}
