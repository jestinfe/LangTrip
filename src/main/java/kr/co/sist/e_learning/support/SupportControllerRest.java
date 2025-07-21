package kr.co.sist.e_learning.support;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
public class SupportControllerRest {

	private final SupportServiceImpl supportService;

	public SupportControllerRest(SupportServiceImpl supportService) {
		this.supportService = supportService;
	}

	@GetMapping("/search-title")
	public List<NoticeDTO> getSuggestedNotices(@RequestParam String keyword) {
		return supportService.findNoticesByKeyword(keyword); // title + id 포함된 DTO 리스트
	}

//	@PostMapping("/faq/hit")
//	public boolean increaseFaqHits(@RequestParam String id) {
//		boolean success = supportService.editFaqHit(id);
//		return success;
//	}
}
