package kr.co.sist.e_learning.support;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SupportController {

	@GetMapping("/support")
	public String index() {
		return "/support/support_index";
	}

	//////////////////////////////////////////////////////////

	private final SupportServiceImpl supportService;

	public SupportController() {
		this.supportService = new SupportServiceImpl(SupportDAOImpl.getInstance());
	}

	/*
	 * @GetMapping("/support/notice/notices") public String
	 * noticeUserStart(@RequestParam(defaultValue = "1") int page, Model model) {
	 * 
	 * int pageSize = 10; List<NoticeDTO> allNotices =
	 * supportService.searchAllNotice("notice");
	 * 
	 * int totalPages = (int) Math.ceil((double) allNotices.size() / pageSize); int
	 * startIndex = (page - 1) * pageSize; int endIndex = Math.min(startIndex +
	 * pageSize, allNotices.size());
	 * 
	 * List<NoticeDTO> paginatedList = allNotices.subList(startIndex, endIndex);
	 * 
	 * List<String> categoryList = new ArrayList<String>(); categoryList.add("결제");
	 * categoryList.add("강의"); categoryList.add("계정"); categoryList.add("보안");
	 * categoryList.add("기타");
	 * 
	 * model.addAttribute("noticeList", paginatedList);
	 * model.addAttribute("categoryList", categoryList);
	 * 
	 * model.addAttribute("currPage", page); model.addAttribute("totalPages",
	 * totalPages);
	 * 
	 * return "notice/notices"; }
	 */

	@GetMapping("/support/notice/notices")
	public String noticeUserStart(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String keyword, Model model) {

		int pageSize = 10;
		List<NoticeDTO> allNotices = supportService.searchAllNotice("notice");

		// 키워드 필터링
		List<NoticeDTO> filteredList = allNotices;
		if (keyword != null && !keyword.trim().isEmpty()) {
			String[] words = keyword.trim().split("\\s+");

			filteredList = allNotices.stream().filter(notice -> {
				String title = notice.getNotice_title();
				String content = notice.getNotice_content();

				// null-safe 처리
				title = (title != null) ? title.toLowerCase() : "";
				content = (content != null) ? content.toLowerCase() : "";

				for (String word : words) {
					word = word.toLowerCase();
					if (title.contains(word) || content.contains(word)) {
						return true;
					}
				}
				return false;
			}).collect(Collectors.toList());
		}

		// 페이지네이션 처리
		int totalPages = (int) Math.ceil((double) filteredList.size() / pageSize);
		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, filteredList.size());
		List<NoticeDTO> paginatedList = filteredList.subList(startIndex, endIndex);

		// 카테고리 목록은 필터링에는 사용하지 않지만 그대로 View로 전달
		List<String> categoryList = List.of("결제", "강의", "계정", "보안", "기타");

		model.addAttribute("noticeList", paginatedList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("currPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword); // 입력값 보존용 (선택)
		model.addAttribute("totalCount", filteredList.size());

		return "support/notice/notices";
	}

	@GetMapping("/support/notice/notice_detail/{id}")
	public String noticeDetail(@PathVariable("id") String id, Model model) {
		NoticeDTO notice = supportService.searchOneNotice("notice", id);
		model.addAttribute("notice", notice);
		return "support/notice/notice_detail";
	}

	@GetMapping("/support/faq/faqs")
	public String faqUserStart() {
//		return "faq/faqs";
		return "index";
	}

	@GetMapping("/support/feedback/about")
	public String feedbackUserStart() {
//		return "feedback/feedback_about";
		return "index";
	}

	@GetMapping("/support/feedback/send")
	public String feedbackSend() {
//		return "feedback/feedback_send";
		return "index";
	}

	//////////////////////////////////////////////////////////

	@GetMapping("/support/admin")
	public String supportManage() {
//		return "support_admin";
		return "index";
	}

	//////////////////////////////////////////////////////////

	@GetMapping("/support/notice/notices/manage")
	public String noticeAdminStart() {
//		return "notice/notice_manage";
		return "index";
	}

	@GetMapping("/support/faq/faqs/manage")
	public String faqAdminStart() {
//		return "faq/faq_manage";
		return "index";
	}

	@GetMapping("/support/feedback/manage")
	public String feedbackAdminStart() {
//		return "feedback/feedback_manage";
		return "index";
	}

	//////////////////////////////////////////////////////////

}// class
