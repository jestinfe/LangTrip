package kr.co.sist.e_learning.quiz;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizOptionDTO {
	 private String quizOptionSeq;
	 private String quizSeq;
	 private int optionOrder;
	 private String content;
	 private String imageName;
	 private String imageAddr;
}
