package kr.co.sist.e_learning.quiz;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.course.CourseService;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private CourseService cs;
    // 강의실 이동 
//    @GetMapping("/quiz/classRoom")
//    public String showClassRoom(HttpSession session, Model model) {
//    	// userSeq  
//    	String userSeq = (String) session.getAttribute("user_seq");
//    	if (userSeq == null) {
//    	    userSeq = "user001";
//    	    session.setAttribute("user_seq", userSeq); 
//    	}
//        
//        // 전체 퀴즈 묶음 목록 조회
//        List<QuizListDTO> quizList = quizService.getAllQuizList(userSeq); 
//
//        model.addAttribute("quizList", quizList);
//        
//        return "quiz/classRoom"; 
//    }
    //userSeq 가져오기
    private Long getUserSeq(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long userSeq=0L;
        if (principal instanceof Long) {
        	userSeq=(Long) principal;
        }
        return userSeq;
    }
    
    // 강의실 : 퀴즈 시작 전 title/language modal창 전용 //수정 필요 userSeq
    @GetMapping("/classRoom/info/data")
    @ResponseBody
    public QuizListDTO QuizModal(@RequestParam String quizListSeq, Long userSeq) {
    	
    	
        return quizService.getQuizListInfo(quizListSeq, userSeq);
    }
    
    // 퀴즈 등록 폼 
    @GetMapping("/quiz/addQuizForm")
    public String showAddQuizForm(@RequestParam("seq") String courseSeq, 
    		Model model) {
    	
    	
    	model.addAttribute("courseSeq", courseSeq);
    	
        return "quiz/addQuizForm"; 
    }
    
    // 퀴즈 등록 처리
    @ResponseBody
    @PostMapping("/addQuizForm")
    public String addQuiz(
        @RequestPart("quizJson") String quizJson,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
        Authentication authentication
    ) throws Exception {
    	

    	Long userSeq = getUserSeq(authentication);
    	
    	// JSON 파싱
    	ObjectMapper mapper = new ObjectMapper();
    	QuizListDTO quizListDTO = mapper.readValue(quizJson, QuizListDTO.class);
    	
    	// 퀴즈 등록자 설정
        quizListDTO.setUserSeq(userSeq);
       

        // DB 저장 서비스 호출
        quizService.addQuiz(quizListDTO, imageFiles);
        if(cs.updateQuizCount(quizListDTO.getCourseSeq()) == 1) {
		};
        return "success";
    }//addQuiz
   
    // 퀴즈 학습 페이지
    @GetMapping("/quiz/playQuiz/{quizListSeq}")
    public String showPlayQuiz(@PathVariable String quizListSeq, Model model,
    		@RequestParam String courseSeq) {
    	
    	
        model.addAttribute("quizListSeq", quizListSeq);
        model.addAttribute("courseSeq", courseSeq);
        return "quiz/playQuiz"; 
    }
    
    //퀴즈 시작
    @ResponseBody
    @GetMapping("/playQuizProcess/{quizListSeq}")
    public Map<String, Object> playQuiz( @PathVariable String quizListSeq,//퀴즈 묶음ID
    		Authentication authentication
    		) {
    	
    	
    	//userSeq 받아오기
    	Long userSeq = getUserSeq(authentication);
    	
    	
    	Map<String, Object> result=quizService.getQuizList(quizListSeq,userSeq);
    	
    	return result;
    }//playQuiz
    
    //퀴즈 응답 문제 모두 풀면 insert 
    @ResponseBody
    @PostMapping("/quizResponse")
    public void saveQuizResponse(@RequestBody QuizResponseDTO qrDTO,
    		Authentication authentication){
    	
    	
    	//userSeq 받아오기
    	Long userSeq = getUserSeq(authentication);
    	qrDTO.setUserSeq(userSeq);
        
    	//정답 체크, 상태 설정, insert
    	quizService.saveQuizResponse(qrDTO);
    			
    }
    
    //퀴즈 학습완료 화면으로 이동
    @GetMapping("/quiz/quizCompleted/{quizListSeq}")
    public String quizCompleted(@PathVariable String quizListSeq, Model model
    		,Authentication authentication, @RequestParam("courseSeq") String courseSeq) {
    	
    	
    	//userSeq 받아오기
    	Long userSeq = getUserSeq(authentication);
    	
    	QuizListDTO qDTO = new QuizListDTO();
    	qDTO.setCourseSeq(courseSeq);
    	
    	Map<String, Object> quizStats = quizService.getQuizCompletionStats(userSeq, quizListSeq);
    	model.addAttribute("courseSeq", courseSeq);
        model.addAllAttributes(quizStats);
        model.addAttribute("qDTO", qDTO);
       
        return "quiz/quizCompleted"; 
    }
    
    //퀴즈 수정 폼 보여주기
    @GetMapping("/quiz/modifyQuizForm/{quizListSeq}")
    public String showModifyQuizForm (Authentication authentication, Model model,
    		  @PathVariable String quizListSeq,
    		  @RequestParam String courseSeq) {
    	
    	
    	try {
    	Long loginUserSeq = getUserSeq(authentication);
    	// DB에서 정보 불러오기
    	QuizListDTO qlDTO = quizService.getQuizListForModify(quizListSeq);
    	if (qlDTO == null) {
    	    return "redirect:/error"; // 또는 적절한 오류 페이지
    	}
    	
    	
    	Long user=qlDTO.getUserSeq();
    	//본인 확인
    	if(!loginUserSeq.equals(user)) {
    		return "redirect:/ui/instroductor_course?userSeq="+loginUserSeq;
    	}
    	
        QuizListDTO qDTO = new QuizListDTO();
    	qDTO.setCourseSeq(courseSeq);
    	
    	model.addAttribute("qlDTO", qlDTO);
    	model.addAttribute("qDTO", qDTO);
    	model.addAttribute("loginUserSeq", loginUserSeq);
    	
        return "quiz/modifyQuizForm";
    	} catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return "redirect:/error"; // 또는 임시 오류 페이지
        }
    }
    
    //퀴즈 수정 처리
    @PostMapping("/modifyQuizForm")
    @ResponseBody
    public String modifyQuiz(
            @RequestParam("quizListSeq") String quizListSeq,
            @RequestParam("quizJson") String quizJson,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            Authentication authentication) {

    	
    	//로그인한 사용자 확인 
    	Long loginUserSeq = getUserSeq(authentication);
    	
    	// DB에서 정보 불러오기
    	QuizListDTO qlDTO = quizService.getQuizListForModify(quizListSeq);
    	Long user=qlDTO.getUserSeq();
    	
    	//퀴즈 작성자인지 확인
    	if(!loginUserSeq.equals(user)) {
    		return "redirect:/ui/instroductor_course?userSeq="+loginUserSeq;
    	}
    	
        try {
			quizService.updateQuiz(quizListSeq, quizJson, imageFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return "success";
    }
    
    //퀴즈 soft delete
    @PostMapping("/deleteQuizList/{quizListSeq}")
    @ResponseBody
    public String deleteQuizList(@PathVariable String quizListSeq,
    		Authentication authentication) {

    	
    	Long loginUserSeq = getUserSeq(authentication);
    	
    	// DB에서 정보 불러오기
    	QuizListDTO qlDTO = quizService.getQuizListForModify(quizListSeq);
    	Long user=qlDTO.getUserSeq();
    	
    	//본인 확인
    	if(!loginUserSeq.equals(user)) {
    		return "accessfailed";
    	}
    	
        quizService.softDeleteAllQuiz(quizListSeq);
        cs.minusQuizCount(quizListSeq);
        return "delete";
    }
    
    //퀴즈 정답,오답 처리
    @PostMapping("/submitAnswer")
    @ResponseBody
    public Map<String, Object> submitAnswer(@RequestBody QuizResponseDTO qrDTO) {
        return quizService.processSubmitAnswer(qrDTO);
    } 
    
}//class


