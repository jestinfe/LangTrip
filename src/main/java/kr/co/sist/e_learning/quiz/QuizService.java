package kr.co.sist.e_learning.quiz;

import java.util.List;

public interface QuizService {
	
	//등록
	void addQuiz(QuizDTO qDTO);  
	 
	//수정
	void modifyQuiz(QuizDTO qDTO);  
	
	//삭제
	boolean removeQuiz(String quizSeq);  
	
	//퀴즈 목록 조회
	List<QuizDTO> getQuizList(String quizListSeq);
	
	//상세 보기
	QuizDTO getQuizDetail(String quizSeq);  
}
