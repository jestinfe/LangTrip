package kr.co.sist.e_learning.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@PostMapping("/{type}/{id}")
	public ResponseEntity<String> submitReport(@PathVariable String type,
			@PathVariable int id,
			@RequestBody ReportDTO reportDTO) {
		System.out.println("🔥🔥🔥 컨트롤러 진입");
		boolean alreadyReported = reportService.checkAlreadyReportedToday(type, id, reportDTO.getReporterId());
		
		if (alreadyReported) {
            return ResponseEntity.badRequest().body("오늘 이미 신고한 콘텐츠입니다.");
        }
		
		reportService.registerReport(type, id, reportDTO);
        return ResponseEntity.ok("신고가 정상적으로 접수되었습니다.");
	}
}
