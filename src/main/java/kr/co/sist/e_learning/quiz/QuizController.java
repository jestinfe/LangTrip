package kr.co.sist.e_learning.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizController {

//	//의존성 주입
//	@Autowired
//	private QuizService qService;
	
	@GetMapping("/addImageQuiz")
	public String showImageQuizAdd() {
		return "/addImageQuizForm";
	}
	
	@GetMapping("/addTextQuiz")
	public String showTextQuizAdd() {
		return "/addTextQuizForm";
	}
	
	@GetMapping("/TextQuizMain")
	public String showTextQuizMain() {
		return "/TextQuizMainForm";
	}
	
	@GetMapping("/ImageQuizMain")
	public String showImageQuizMain() {
		return "/ImageQuizMainForm";
	}
	
	@GetMapping("/quizCompleted")
	public String showQuizCompleted() {
		return "/quizCompleted";
	}
}
