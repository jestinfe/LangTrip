package kr.co.sist.e_learning.usercourse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.admin.account.AdminAccountController;
import kr.co.sist.e_learning.course.CombineDTO;
import kr.co.sist.e_learning.course.CourseDTO;
import kr.co.sist.e_learning.course.CourseService;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.quiz.QuizService;
import kr.co.sist.e_learning.video.VideoDTO;
import kr.co.sist.e_learning.video.VideoService;

@Controller
public class UserCourseController {

    private final AdminAccountController adminAccountController;

	@Autowired
	private UserCourseService ucs;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private VideoService vs;
	
	@Autowired
	private QuizService qs;

    UserCourseController(AdminAccountController adminAccountController) {
        this.adminAccountController = adminAccountController;
    }
	
	@GetMapping("/ui/user_lecture")
	public String userLecture(@RequestParam("seq") String courseSeq, Model model,
			 Authentication authentication) {
		
		
		
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		
		
		
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
//		List<QuizListDTO> quizList = qs.searchQuizByCourseSeq(courseSeq);
//		List<QuizListDTO> quizSeq = qs.searchQuizSeqByCoursSEq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
		 List<CombineDTO> combinedList = new ArrayList<CombineDTO>();
		    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		    // videoList를 CombinedItem으로 변환하여 추가
		    for (VideoDTO video : videoList) {
		    	System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		        combinedList.add(new CombineDTO("video", video.getVideoSeq(), video.getCourseSeq(), video.getUploadDate()));
		    }
		    
		    // quizList를 CombinedItem으로 변환하여 추가
		    for (QuizListDTO quiz : quizList) {
		    	System.out.println("quizasddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		        combinedList.add(new CombineDTO("quiz", quiz.getQuizListSeq(), courseSeq, quiz.getUploadDate()));
		    }
		    System.out.println(combinedList.toString());
		    // createdAt 기준으로 정렬
		    combinedList.sort(Comparator.comparing(CombineDTO::getUploadDate));
		    System.out.println(combinedList.toString());
		    // 모델에 combinedList 전달
		    model.addAttribute("combinedList", combinedList);
		
		model.addAttribute("userSeq", userSeq);
		model.addAttribute("courseData", cDTO);
		model.addAttribute("videoList", videoList);
		model.addAttribute("quizList", quizList);
		return "ui/user_lecture";
	}
	
	@GetMapping("/ui/user_course")
	public String userCourse() {
		
		
		return "ui/user_course";
	}
	
	@PostMapping("/user/course_enroll")
	public ResponseEntity<?> registerCourse(@RequestParam("courseSeq") String courseSeq,
	                                        Authentication authentication) {
	    Object principal = authentication.getPrincipal();
	    Long userSeq = null;

	    if (principal instanceof Long) {
	        userSeq = (Long) principal;
	    }


	    try {
	        // 강의를 만든 사람의 userSeq 조회
	        CourseDTO cDTO = cs.selectUserSeqByCourseSeq(courseSeq);
	        Map<String, Object> userData = new HashMap<String, Object>();
	        userData.put("userSeq", userSeq);
	        userData.put("courseSeq", courseSeq);
	        
	        if (cDTO.getUserSeq() == null) {
	            return ResponseEntity.badRequest().body(Map.of("msg", "강의 정보가 없습니다."));
	        }

	        // 1. 자기가 만든 강의는 수강 불가
	        if (cDTO.getUserSeq().equals(userSeq)) {
	            return ResponseEntity.badRequest().body(Map.of("msg", "자신이 만든 강의는 수강할 수 없습니다."));
	        }

	        // 2. 이미 수강 중인지 확인
//	        boolean alreadyEnrolled = ucs.checkUserAlreadyEnrolled(courseSeq, userSeq); // 추가 필요
	        int alreadyEnrolled = ucs.selectAlreadyEnrollCourse(userData);
	        if (alreadyEnrolled > 0) {
	            return ResponseEntity.badRequest().body(Map.of("msg", "이미 수강 중인 강의입니다."));
	        }

	        // 3. 수강 등록
	        UserCourseDTO ucDTO = new UserCourseDTO();
	        ucDTO.setCourseSeq(courseSeq);
	        ucDTO.setUserSeq(userSeq);

	        int result = ucs.addUserCourse(ucDTO);

	        if (result == 0) {
	            return ResponseEntity.internalServerError().body(Map.of("msg", "수강 등록에 실패했습니다."));
	        }else {
	        cs.plusEnrollCount(courseSeq);
	        }
	        return ResponseEntity.ok(Map.of("msg", "수강완료"));

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body(Map.of("msg", "서버 오류 발생"));
	    }
	}
	
	@PostMapping("/user/course_cancel")
	@ResponseBody
	public ResponseEntity<?> cancelCourse(
	        @RequestParam("courseSeq") String courseSeq,
	        Authentication authentication) {

	    Object principal = authentication.getPrincipal();
	    Long userSeq = null;

	    if (principal instanceof Long) {
	        userSeq = (Long) principal;
	    }

	    try {
	    	 Map<String, Object> userData = new HashMap<String, Object>();
		        userData.put("userSeq", userSeq);
		        userData.put("courseSeq", courseSeq);
	        // 수강 여부 확인
	        int alreadyEnrolled = ucs.selectAlreadyEnrollCourse(userData);

	        if (alreadyEnrolled == 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("msg", "수강 중인 강의가 아닙니다."));
	        }

	        // 수강 취소 진행
	        UserCourseDTO dto = new UserCourseDTO();
	        dto.setCourseSeq(courseSeq);
	        dto.setUserSeq(userSeq);

	        int deleted = ucs.deleteUserCourse(dto);

	        if (deleted > 0) {
	            return ResponseEntity.ok(Map.of("msg", "수강이 취소되었습니다."));
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body(Map.of("msg", "수강 취소 실패"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError()
	                             .body(Map.of("msg", "서버 오류 발생"));
	    }
	}

	
//	@PostMapping("/user/course_enroll")
//	public ResponseEntity<?> registerCourse(@RequestParam("courseSeq") String courseSeq,
//			Authentication authentication){
//		Object principal = authentication.getPrincipal();
//		Long userSeq = null;
//		if(principal instanceof Long) {
//			userSeq = (Long) principal;
//		}
//		
//		//강의 만든사람 userSeq검색
//		
//		try {
//			CourseDTO cDTO = cs.selectUserSeqByCourseSeq(courseSeq);
//			UserCourseDTO ucDTO = new UserCourseDTO();
//			ucDTO.setCourseSeq(courseSeq);
//			ucDTO.setUserSeq(userSeq);
//			
//			if (cDTO.getUserSeq().equals(userSeq)) {
//	            return ResponseEntity.badRequest().body(Map.of("msg", "자신이 만든 강의는 수강할 수 없습니다."));
//	        }else {
//		    int result = ucs.addUserCourse(ucDTO);
//		    if(result==0) {
//		    }
//			}
//		} catch (Exception e) {
//		    System.err.println("🔥 예외 발생: " + e.getMessage());
//		    e.printStackTrace();  // 정확한 오류 위치 확인
//		}
//		
//		
//		
//		return ResponseEntity.ok(Map.of("msg","수강완료"));
//		
//	}
	
	
	@GetMapping("/user/showUserCourses")
	@ResponseBody
	public Map<String, Object> showUserCourse(Authentication authentication,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int limit){
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		
		int offset = (page -1)*limit;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userSeq);
		param.put("offset", offset);
		param.put("limit", limit);
		List<UserCourseDTO> paginationList = ucs.searchUserCourseByPage(param);
		
		int totalCount = ucs.searchUserCourseCount(userSeq);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userCourseList", paginationList);
		result.put("totalCount", totalCount);
		result.put("page", page);
	    result.put("limit", limit);
		
		List<UserCourseDTO> list = ucs.searchUserCourseByUserId(userSeq);
		for(UserCourseDTO uDTO : list) {
		}
		
		result.put("courses", list);
		return result;
	}
	
	@GetMapping("/ui/user_delete_course")
	@ResponseBody
	public Map<String, String> goToDeleteCourse(@RequestParam("seq") String courseSeq, Authentication authentication) {
	    Map<String, String> response = new HashMap<>();

	    try {
	        // 인증된 사용자 정보를 가져옴
	        Object principal = authentication.getPrincipal();
	        Long userSeq = null;
	        if (principal instanceof Long) {
	            userSeq = (Long) principal;
	        }

	        // 강의 삭제를 위한 Map 생성
	        Map<String, Object> map = new HashMap<>();
	        map.put("courseSeq", courseSeq);
	        map.put("userSeq", userSeq);
	        System.out.println("courseSeq: =" +courseSeq);
	        System.out.println("courseSeq: =" +userSeq);
	        // 강의 삭제 처리
	        ucs.deleteRegisterCourse(map);  // 삭제 성공

	        // 성공 메시지 설정
	        response.put("message", "강의가 성공적으로 삭제되었습니다.");
	    } catch (Exception e) {
	        // 삭제 실패 메시지 설정
	    	
	    	System.out.println("디비 오류: " + e.getMessage());
	        response.put("message", "강의 삭제에 실패했습니다. 오류가 발생했습니다.");
	    }

	    // JSON 형태로 반환
	    return response;  // JSON 형식으로 응답
	}
	
	@GetMapping("/user/user_pagination")
	public String showUserCourses(
	        @RequestParam(defaultValue = "1") int page, 
	        @RequestParam(defaultValue = "4") int limit, 
	        Model model, Authentication authentication) {

	    // 인증된 사용자 정보
		  Object principal = authentication.getPrincipal();
	        Long userSeq = null;
	        if (principal instanceof Long) {
	            userSeq = (Long) principal;
	        }

	     Map<String, Object> map = new HashMap<String, Object>();
	     map.put("page", page);
	     map.put("limit", limit);
	     map.put("userSeq", userSeq);
	    // 전체 강의 개수와 페이징된 강의 목록 조회
	    int totalCourses = ucs.getTotalCourses(userSeq);  // 총 강의 개수
	    UserCourseDTO courses = ucs.getCoursesBypage(map);  // 해당 페이지의 강의 목록

	    int totalPages = (int) Math.ceil((double) totalCourses / limit);  // 전체 페이지 수 계산

	    // 모델에 데이터 추가
	    model.addAttribute("courses", courses);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("limit", limit);

	    return "mypage/user_course";  // Thymeleaf에서 해당 페이지 렌더링
	}

	
	
	@GetMapping("/ui/user_registered_course")
	public String goToRegisterdCourse(@RequestParam("seq") String courseSeq, Model model,
			 Authentication authentication) {
		
	
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		
		
		
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
//		List<QuizListDTO> quizList = qs.searchQuizByCourseSeq(courseSeq);
//		List<QuizListDTO> quizSeq = qs.searchQuizSeqByCoursSEq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
		 List<CombineDTO> combinedList = new ArrayList<CombineDTO>();
		    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		    // videoList를 CombinedItem으로 변환하여 추가
		    for (VideoDTO video : videoList) {
		    	System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		        combinedList.add(new CombineDTO("video", video.getVideoSeq(), video.getCourseSeq(), video.getUploadDate()));
		    }
		    
		    // quizList를 CombinedItem으로 변환하여 추가
		    for (QuizListDTO quiz : quizList) {
		    	System.out.println("quizasddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		        combinedList.add(new CombineDTO("quiz", quiz.getQuizListSeq(), courseSeq, quiz.getUploadDate()));
		    }
		    System.out.println(combinedList.toString());
		    // createdAt 기준으로 정렬
		    combinedList.sort(Comparator.comparing(CombineDTO::getUploadDate));
		    System.out.println(combinedList.toString());
		    // 모델에 combinedList 전달
		    model.addAttribute("combinedList", combinedList);
		
		model.addAttribute("userSeq", userSeq);
		model.addAttribute("courseData", cDTO);
		model.addAttribute("videoList", videoList);
		model.addAttribute("quizList", quizList);
		return "ui/user_registered_course";
	}
	
	
    @GetMapping("/courses")
    public String listPublicCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "uploadDate,desc") String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            Model model) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("sort", sort);
        params.put("category", category);
        params.put("difficulty", difficulty);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        PageResponseDTO<UserCourseListDisplayDTO> responseDTO = ucs.getPublicCourses(params);

        model.addAttribute("courseList", responseDTO.getList());
        model.addAttribute("currentPage", responseDTO.getPage());
        model.addAttribute("totalPages", responseDTO.getTotalPages());
        model.addAttribute("startPage", responseDTO.getStartPage());
        model.addAttribute("endPage", responseDTO.getEndPage());
        model.addAttribute("totalCount", responseDTO.getTotalCnt());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);
        model.addAttribute("category", category);
        model.addAttribute("difficulty", difficulty);

        return "user/course/course_list";
    }
}

