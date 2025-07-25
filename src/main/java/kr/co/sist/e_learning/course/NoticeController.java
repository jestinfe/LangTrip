package kr.co.sist.e_learning.course;

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
@RequestMapping("/notice")
public class NoticeController {

	 @Autowired
	    private NoticeService noticeService;

	    @PostMapping("/save")
	    public ResponseEntity<String> save(@RequestBody NoticeDTO nDTO) {
//	        noticeService.insertNotice(nDTO);
	        return ResponseEntity.ok("저장 성공");
	    }

//	    @GetMapping("/get")
//	    public ResponseEntity<String> getNotice(@RequestParam int courseSeq) {
//	        String content = noticeService.getNoticeContent(courseSeq);
//	        return ResponseEntity.ok(content != null ? content : "");
//	    }
	
}
