package kr.co.sist.e_learning.course;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.quiz.QuizService;
import kr.co.sist.e_learning.video.VideoDTO;
import kr.co.sist.e_learning.video.VideoService;

@Controller
public class CourseController {

	@Value("${file.upload-dir.courseImg}")  
    private String courseImgPath;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private VideoService vs;

	@Autowired
	private QuizService qs;
	
//	@GetMapping("ui/instroductor_course")
//	public String myLecture(HttpSession session) {
//		
//		if(session.getAttribute("userSeq") == null) {
//			session.setAttribute("userSeq", "test123");
//		}
//		
//		
//		
//		return "ui/instroductor_course";
//	}
	
	
	
	
	
	
	@GetMapping("/ui/my_lecture")
	    public ResponseEntity<?> instroductorPage(Authentication authentication,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "12") int limit) {
		
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long)principal;
		}
		
		int offset = (page - 1)*limit;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userSeq", userSeq);
		param.put("offset", offset);
		param.put("limit", limit);
		
		List<CourseDTO> courseList = cs.selectCourseByPage(param);
		int totalCount = cs.selectCourseCount(userSeq);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("courseList", courseList);
	    result.put("totalCount", totalCount);
	    result.put("page", page);
	    result.put("limit", limit);
	    result.put("msg", "pagination");
		
		return ResponseEntity.ok(result);
	}

	
	@PostMapping("/ui/register_course")
	public ResponseEntity<?> registerCourse(@RequestParam("thumbnail") MultipartFile mf,
			@ModelAttribute CourseDTO cDTO,
			Authentication authentication)throws Exception{
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		String thumbNail="";
		
		if(mf.getContentType().startsWith("image") && !mf.isEmpty()) {
			int maxSize=1024*1024*5;
//			if(mf.getSize()>maxSize) {
//				throw new Exception("업로드한 파일의 크기는 최대 5MByte까지 입니다.");
//			}//end if
			String originalFileName = mf.getOriginalFilename();
			
			File dir = new File(courseImgPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}//end if
			
			File uploadFile = new File(courseImgPath, originalFileName);
			String fileName = "";
			String fileExt = "";
			int fileSeperator=originalFileName.lastIndexOf(".");
			fileName = originalFileName.substring(0, fileSeperator);
			if(originalFileName.contains(".")) {
				fileExt = originalFileName.substring(fileSeperator+1);
			}
			
			int index = 1;
//			while(uploadFile.exists()) {
//				String newName = fileName+"("+index+")."+fileExt;
//				uploadFile = new File(courseImgPath+File.separator+newName);
//				index++;
//			}
			mf.transferTo(uploadFile);
			thumbNail=uploadFile.getName();
		
		
		}
//		String userSeq = (String)session.getAttribute("userSeq");
//		cDTO.setUserSeq(userSeq);
		cDTO.setUserSeq(userSeq);
		
		cDTO.setThumbnailName(thumbNail);
		cDTO.setThumbnailPath("/courseImg/"+thumbNail);
		cDTO.setUploadDate(new Date());
		
		cDTO.setFlag("T");
		int result = cs.addCourse(cDTO);
		if (result == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                         .body(Map.of("msg", "DB 삽입 실패"));
		}
		
		return ResponseEntity.ok(Map.of("msg", "강의 등록 완료", "courseData", cDTO));
	}
	
	
	
	@GetMapping("/upload/upload_course")
	public String showCourseForm(@RequestParam("seq") String courseSeq, Model model) {
	    // 예시로 videoList와 quizList 가져오기
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
	    
	    // 비디오와 퀴즈 리스트를 하나로 합치기
	    List<CombineDTO> combinedList = new ArrayList<CombineDTO>();
	    // videoList를 CombinedItem으로 변환하여 추가
	    for (VideoDTO video : videoList) {
	        combinedList.add(new CombineDTO("video", video.getVideoSeq(), video.getCourseSeq(), video.getUploadDate()));
	    }
	    
	    // quizList를 CombinedItem으로 변환하여 추가
	    for (QuizListDTO quiz : quizList) {
	        combinedList.add(new CombineDTO("quiz", quiz.getQuizListSeq(), courseSeq, quiz.getUploadDate()));
	    }
	    // createdAt 기준으로 정렬
	    combinedList.sort(Comparator.comparing(CombineDTO::getUploadDate));
	    // 모델에 combinedList 전달
	    model.addAttribute("combinedList", combinedList);
	    model.addAttribute("courseData", cDTO);
	    model.addAttribute("videoList", videoList);
	    return "ui/upload_frm";
	}
		
	@GetMapping("/upload/upload_update")
	public String updateCourse(HttpSession session, Model model) {
		CourseDTO cDTO = (CourseDTO) session.getAttribute("courseToEdit");
		model.addAttribute("cDTO", cDTO);
		return "ui/update_course";
	}
	
	@PostMapping("/upload/storeCourseSession")
	@ResponseBody
	public void storeCourseSession(@RequestBody CourseDTO cDTO, HttpSession session) {
		session.setAttribute("courseToEdit", cDTO);
	}
	
	@PostMapping("/upload/modify_course")
	public String modifyCourse(@RequestParam("thumbnail") MultipartFile mf
			,@ModelAttribute CourseDTO cDTO, Model model, HttpSession session) throws Exception{
		String thumbNail="";
		if(mf.getContentType().startsWith("image") && !mf.isEmpty()) {
			int maxSize=1024*1024*5;
			if(mf.getSize()>maxSize) {
				model.addAttribute("msg", "다시 이미지 넣으셈");
				throw new Exception("업로드한 파일의 크기는 최대 5MByte까지 입니다.");
			
			}//end if
			String originalFileName = mf.getOriginalFilename();
			File dir = new File(courseImgPath);
			File uploadFile = new File(courseImgPath+File.separator+originalFileName);
			String fileName = "";
			String fileExt = "";
			int fileSeperator=originalFileName.lastIndexOf(".");
			fileName = originalFileName.substring(0, fileSeperator);
			if(originalFileName.contains(".")) {
				fileExt = originalFileName.substring(fileSeperator+1);
			}
			mf.transferTo(uploadFile);
			thumbNail=uploadFile.getName();

		}
		cDTO.setThumbnailName(thumbNail);
		cDTO.setThumbnailPath("/upload/img/"+thumbNail);
		
		
		int result = cs.modifyCourse(cDTO);
		if(result>0) {
		}else {
		}
		
//		session.setAttribute("courseToEdit", cDTO);
		model.addAttribute("cDTO", cDTO);
		 
		
		return "ui/update_course";
	}
	
	@PostMapping("/upload/delete_course")
	@ResponseBody
	public String deleteCourse(@RequestParam("seq")String courseSeq, Model model) {
		
		int result = cs.removeCourse(courseSeq);
		String msg="";
		if(result>0) {
			msg ="success";
			cs.minusVideoCount(courseSeq);
			System.out.println("삭제 성공");
		}else {
			msg ="fail";
		}
		
		return msg;
	}
	

}
