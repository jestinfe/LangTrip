package kr.co.sist.e_learning.quiz;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizListDTO {
	 private String quizListSeq;
	 private String courseSeq;     
	 private String langCategory;  
	 private String title;       
	 private Date uploadDate;
}
