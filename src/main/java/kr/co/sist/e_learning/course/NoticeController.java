package kr.co.sist.e_learning.course;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sist.e_learning.support.NoticeDTO;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

	 @Autowired
	    private NoticeService noticeService;

	    @PostMapping("/save")
	    public void saveNotice(@RequestBody CourseNoticeDTO courseNotice
	    			) {
	        // 공지 저장 서비스 호출
	    	courseNotice.setType("NOTICE");
	        System.out.println(courseNotice.getContent()); // 서버에서 받은 공지 내용 확인용
	        noticeService.saveNotice(courseNotice); // 실제 공지 저장
	    }

	    // 최신 공지 조회 API
	    @GetMapping("/latest")
	    public CourseNoticeDTO getLatestNotice(@RequestParam String courseSeq) {
	        // 최신 공지 조회 서비스 호출
	    	System.out.println(courseSeq);
	        CourseNoticeDTO cnDTO = noticeService.getLatestNotice(courseSeq);
	        return cnDTO;
	    }
	    
	    @PostMapping("/saveLearning")
	    public void saveLearning(@RequestBody CourseNoticeDTO courseLearning
	    			) {
	        // 공지 저장 서비스 호출
	    	courseLearning.setType("LEARNING");
	        System.out.println(courseLearning.getContent()); // 서버에서 받은 공지 내용 확인용
	        noticeService.saveNotice(courseLearning); // 실제 공지 저장
	    }

	    // 최신 공지 조회 API
	    @GetMapping("/latestLearning")
	    public CourseNoticeDTO getLatestLearning(@RequestParam String courseSeq) {
	        // 최신 공지 조회 서비스 호출
	    	System.out.println(courseSeq);
	        CourseNoticeDTO cnDTO = noticeService.getLatestLearning(courseSeq);
	        return cnDTO;
	    }

	
}
