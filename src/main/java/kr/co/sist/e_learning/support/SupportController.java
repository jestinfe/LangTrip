package kr.co.sist.e_learning.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SupportController {

	private final NoticeDTO noticeDTO;

	private final SupportServiceImpl supportService;

	public SupportController(SupportServiceImpl supportService, NoticeDTO noticeDTO) {
		this.supportService = supportService;
		this.noticeDTO = noticeDTO;
	}

//	@GetMapping("/support")
//	public String index(Model model) {
//		List<FaqDTO> topFaqs = supportService.searchTop3Faq();
//		model.addAttribute("faqList", topFaqs);
//		return "support/support_index";
//	}
	@GetMapping("/support")
	public String index(Model model) {
		List<FaqDTO> topFaqs = supportService.searchTop3Faq();
		model.addAttribute("faqList", topFaqs);
		return "support/support_index";
	}

	// ---------------------------------------------------------------------------------------------

	// 사용자 : 공지사항 전체 조회(notices.html)
	@GetMapping("/support/notice")
	public String noticeForUser(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String keyword, Model model) {

		int pageSize = 10;
		List<NoticeDTO> noticeList = supportService.getPaginatedNotices(keyword, page, pageSize);
		int totalCount = supportService.getFilteredNoticeCount(keyword);
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		model.addAttribute("noticeList", noticeList);
//		model.addAttribute("categoryList", List.of("결제", "강의", "계정", "보안", "기타"));
		model.addAttribute("currPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalCount", totalCount);

		return "support/notice/notices";
	}

	// 사용자 : 공지사항 상세 조회(notice_detail.html)
	@GetMapping("/support/notice/{id}")
	public String noticeDetailForUser(@PathVariable("id") String id, @RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String keyword, Model model) {

		NoticeDTO notice = supportService.searchOneNotice(id);

		// 예외처리: 없는 공지일 경우
		if (notice == null) {
			model.addAttribute("errorMessage", "존재하지 않는 공지사항입니다.");
			return "support/notice/notice_error"; // 또는 notice 목록으로 redirect
		}

		model.addAttribute("notice", notice);
		model.addAttribute("currPage", page); // 페이지 정보 유지
		model.addAttribute("keyword", keyword); // 검색어 유지

		return "support/notice/notice_detail";
	}

	// ---------------------------------------------------------------------------------------------

	// 사용자 : FAQ 전체 조회(faqs.html)
	@GetMapping("/support/faq")
	public String faqUserStart(@RequestParam(required = false) String openId, Model model) {

		List<FaqtypeDTO> faqtypeList = supportService.searchAllFaqtype();
		List<FaqDTO> allFaqList = supportService.searchAllFaq();
		model.addAttribute("faqList", allFaqList);

		Map<String, List<FaqDTO>> groupedFaqs = new LinkedHashMap<>();
		for (FaqDTO faq : allFaqList) {
			groupedFaqs.computeIfAbsent(faq.getFaqtype_name(), k -> new ArrayList<>()).add(faq);
		}

		model.addAttribute("faqtypeList", faqtypeList);
		model.addAttribute("groupedFaqs", groupedFaqs);
		model.addAttribute("openId", openId); // 이 값이 JS에서 사용됨
		return "support/faq/faqs";
	}

	// 사용자 : FAQ(AJAX 요청용) = 특정 유형의 FAQ만 반환
	@GetMapping("/support/faq/{typeId}")
	public String loadFaqByType(@PathVariable("typeId") String typeId, Model model) {
		List<FaqDTO> faqList;
		List<FaqtypeDTO> faqtypeList;

		if ("all".equalsIgnoreCase(typeId)) {
			faqList = supportService.searchAllFaq();
		} else {
			faqList = supportService.searchFaqByTypeId(typeId);
		}

		Map<String, List<FaqDTO>> groupedFaqs = new LinkedHashMap<>();
		for (FaqDTO faq : faqList) {
			groupedFaqs.computeIfAbsent(faq.getFaqtype_name(), k -> new ArrayList<>()).add(faq);
		}

		model.addAttribute("groupedFaqs", groupedFaqs);
		return "support/faq/faq-item :: faqItemFragment";
	}

	// 사용자 : FAQ 조회수 증가
	@PostMapping("/faq/hit")
	@ResponseBody
	public boolean increaseFaqHits(@RequestParam String id) {
		boolean success = supportService.editFaqHit(id);
		return success;
	}

	// ---------------------------------------------------------------------------------------------

	// 사용자 : 피드백 소개(feedback_about.html)
	@GetMapping("/support/feedback/about")
	public String feedbackUserStart() {
		return "support/feedback/feedback_about";
	}

	// 사용자 : 피드백 POST 제출( feedback_send.html -> feedback_finished.html )
	@PostMapping("/support/feedback/submit")
	public String submitFeedback(@ModelAttribute FeedbackDTO feedbackDTO) {
		System.out.println("실명(1)/익명(0) 피드백 구분: " + feedbackDTO.getFeedback_style());
		supportService.addFeedback(feedbackDTO);
		return "redirect:/support/feedback/finished"; // 제출 완료 페이지
	}

	// 사용자 : 피드백 POST 제출( feedback_send.html -> feedback_finished.html )
	@GetMapping("/support/feedback/finished")
	public String feedbackFinished() {
		return "support/feedback/feedback_finished";
	}

//	// 사용자 : 피드백 작성(feedback_send.html)
//	@GetMapping("/support/feedback/send")
//	public String feedbackSend(Model model) {
//
//		Map<String, String> sessionUser = new HashMap<String, String>();
//		sessionUser.put("user_id", "paul");// 샘플 로그인
//
//		model.addAttribute("sessionUser", sessionUser);
//		model.addAttribute("sessionUser", null);
//
//		return "support/feedback/feedback_send";
//	}

	// 사용자 : 피드백 작성(feedback_send.html)
	@GetMapping("/support/feedback/send")
	public String feedbackSend(Model model) {

		Map<String, String> sessionUser = new HashMap<>();
		sessionUser.put("user_id", "paul"); // 샘플 로그인

//		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("sessionUser", null);

		// faqtype 리스트 주입
		List<FaqtypeDTO> faqtypeList = supportService.searchAllFaqtype();
		model.addAttribute("faqtypeList", faqtypeList);

		return "support/feedback/feedback_send";
	}

	// ---------------------------------------------------------------------------------------------

	// 지원센터 관리 : 메인 (support_admin.html)
//	@GetMapping("/support/admin")
//	public String supportManage1() {
//		return "support/admin/support_admin";
//	}

	// 지원센터 관리 : 조회수 높은 FAQ 추천
	@GetMapping("/faq/slider")
	public String getFaqSlider(Model model) {
		List<FaqDTO> topFaqs = supportService.searchTop3Faq();
		model.addAttribute("faqList", topFaqs);
		return "faq-slider"; // 템플릿명
	}

	// ---------------------------------------------------------------------------------------------

	// 공지사항 관리 : 전체 조회 (support_admin_notices.html)
//	@GetMapping("/support/admin/notice")
	@GetMapping("/admin/support")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String supportNotcieAdmin(Model model) {

		List<NoticeDTO> noticeList = supportService.searchAllNoticeAdmin();

		model.addAttribute("noticeList", noticeList);

		return "support/admin/support_admin_notices";
	}

	// 공지사항 관리 : 상세 조회 (support_admin_notice_detail.html)
	@GetMapping("/support/admin/notice/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String noticeDetailAdmin(@PathVariable("id") String id, Model model) {
		NoticeDTO notice = supportService.searchOneNotice(id);
		model.addAttribute("notice", notice);
		return "support/admin/support_admin_notice_detail";
	}

	// --------------------

	@GetMapping("/support/admin/notice/edit/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String editNoticeAdminPage(@PathVariable("id") String id, Model model) {
		NoticeDTO notice = supportService.searchOneNotice(id); // 해당 공지 조회
		model.addAttribute("notice", notice);
		return "support/admin/support_admin_notice_detail_edit2"; // 수정 페이지 템플릿 경로
	}

	@PostMapping("/support/admin/notice/update/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String updateNotice(@PathVariable("id") String id, NoticeDTO notice, RedirectAttributes redirectAttributes) {
		notice.setNotice_id(id); // URL에서 넘어온 ID를 DTO에 명시적으로 설정

		boolean success = supportService.updateNotice(notice);
		if (success) {
			redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 수정되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("error", "공지사항 수정에 실패했습니다.");
		}
		return "redirect:/support/admin/notice/" + id;
	}

	// --------------------

	@PostMapping("/support/admin/notice/delete/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String deleteNoticeAdmin(@PathVariable("id") String id) {
		supportService.editNoticeStatusInactive(id); // 상태 업데이트(비활성)
		return "redirect:/support/admin/notice";

	}

	@PostMapping("/support/admin/notice/active/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String activeNoticeAdmin(@PathVariable("id") String id) {
		supportService.editNoticeStatusActive(id); // 상태 업데이트(활성)
		return "redirect:/support/admin/notice";

	}

	// --------------------

	@PostMapping("/support/admin/notice/fix/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String fixNoticeAdmin(@PathVariable("id") String id) {

		boolean flag = supportService.editNoticeFixFlag(id, "Y"); // 고정으로 설정
		if (flag) {
			System.out.println("공지사항 공지 고정 완료 ");
		} else {
			System.out.println("에러발생 ");
		}

		return "redirect:/support/admin/notice";
	}

	@PostMapping("/support/admin/notice/unfix/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String unfixNoticeAdmin(@PathVariable("id") String id) {
		boolean flag = supportService.editNoticeFixFlag(id, "N"); // 고정 해제
		if (flag) {
			System.out.println("공지사항 공지 해제 완료 ");
		} else {
			System.out.println("에러발생 ");
		}
		return "redirect:/support/admin/notice";
	}

	// ---------------------------------------------------------------------------------------------

	// FAQ 관리 (support_admin.html)
	@GetMapping("/support/admin/faq")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String supportFaqAdmin() {
		return "support/admin/support_admin_faq";
	}

	// ---------------------------------------------------------------------------------------------

	// 관련 통계 : 현황 조회(support_admin_feedback.html)
	@GetMapping("/support/admin/feedback")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String supportAdminFeedback(Model model) {

		Map<String, Integer> typeCountMap = new HashMap<>();
//		List<FeedbackDTO> graphData = supportService.searchAllNewFeedback();
//		List<FeedbackDTO> graphData = supportService.searchAllFeedback();
		List<FeedbackDTO> graphData = supportService.searchTotalFeedback();
		for (FeedbackDTO dto : graphData) {
			String type = dto.getFeedback_type(); // 예: "결제", "강좌"

			if (type.equals("3000")) {
				type = "결제";
			}
			if (type.equals("3001")) {
				type = "강좌";
			}
			if (type.equals("3002")) {
				type = "학습";
			}
			if (type.equals("3003")) {
				type = "계정";
			}
			if (type.equals("3004")) {
				type = "기타";
			}
			if (type.equals("3005")) {
				type = "기능";
			}

			typeCountMap.put(type, typeCountMap.getOrDefault(type, 0) + 1);
		}
		model.addAttribute("feedbackChartData", typeCountMap);

		int maxSends = supportService.searchMaxFeedbackSends();
		model.addAttribute("maxSends", maxSends);

		// -----

		List<FaqDTO> graphData2 = supportService.searchAllFaqDTO();
		List<String> faqLabels = new ArrayList<>();
		List<Integer> faqHits = new ArrayList<>();

		for (FaqDTO dto : graphData2) {
			faqLabels.add(dto.getFaq_id());
			faqHits.add(dto.getFaq_hits()); // 조회수
		}
		model.addAttribute("faqChartLabels", faqLabels);
		model.addAttribute("faqChartValues", faqHits);

		// -----

		Map<String, Integer> type1Data = new LinkedHashMap<>();
		List<FaqDTO> faqList01 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList01) {
			if ("강좌".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type1Data.put(faqId, hits);
			}
		}
		model.addAttribute("type1Data", type1Data);

		Map<String, Integer> type2Data = new LinkedHashMap<>();
		List<FaqDTO> faqList02 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList02) {
			if ("학습".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type2Data.put(faqId, hits);
			}
		}
		model.addAttribute("type2Data", type2Data);

		Map<String, Integer> type3Data = new LinkedHashMap<>();
		List<FaqDTO> faqList03 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList03) {
			if ("결제".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type3Data.put(faqId, hits);
			}
		}
		model.addAttribute("type3Data", type3Data);

		Map<String, Integer> type4Data = new LinkedHashMap<>();
		List<FaqDTO> faqList04 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList04) {
			if ("계정".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type4Data.put(faqId, hits);
			}
		}
		model.addAttribute("type4Data", type4Data);

		Map<String, Integer> type5Data = new LinkedHashMap<>();
		List<FaqDTO> faqList05 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList05) {
			if ("기타".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type5Data.put(faqId, hits);
			}
		}
		model.addAttribute("type5Data", type5Data);

		Map<String, Integer> type6Data = new LinkedHashMap<>();
		List<FaqDTO> faqList06 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList06) {
			if ("기능".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type6Data.put(faqId, hits);
			}
		}
		model.addAttribute("type6Data", type6Data);

		// -----

		int maxHits = supportService.searchMaxFaqHits() * 2;
		model.addAttribute("maxHits", maxHits);

		return "support/admin/support_admin_feedback";
	}

	// 피드백 관리 : 현황 조회(support_admin_feedback.html)
	@GetMapping("/support/admin/feedback2")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String supportAdminFeedback2(Model model) {

		List<FeedbackDTO> newFeedbackList = supportService.searchAllNewFeedback();
		List<FeedbackDTO> niceFeedbackList = supportService.searchAllNiceFeedback();

		model.addAttribute("newFeedbackList", newFeedbackList);
		model.addAttribute("niceFeedbackList", niceFeedbackList);

		return "support/admin/support_admin_feedback2";
	}

	// 피드백 관리 : 전체 조회 (support_admin_feedback2.html)
	@GetMapping("/support/admin/feedbacks")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String supportAdminFeedbacks(Model model) {

		List<FeedbackDTO> feedbackList = supportService.searchAllFeedback();
		model.addAttribute("feedbackList", feedbackList);

		return "support/admin/support_admin_feedbacks";
	}

	@GetMapping("/support/admin/feedback_detail/{id}")
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String getFeedbackDetailFragment(@PathVariable String id, Model model) {
		FeedbackDTO feedback = supportService.searchOneFeedback(id);
		model.addAttribute("feedback", feedback);
		return "support/admin/feedback_detail_fragment :: fragment";
	}

	@PostMapping("/support/admin/feedback/stepUpdate")
	@ResponseBody
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String updateStep(@RequestParam("id") String id) {
		boolean success = supportService.editFeedbackStep(id);
		return success ? "success" : "fail";
	}

	@PostMapping("/support/admin/feedback/refuse")
	@ResponseBody
	@PreAuthorize("hasAnyRole('SUPPORT', 'SUPER')")
	public String feedbackRefuse(@RequestParam("id") String id) {
		boolean success = supportService.editFeedbackRefuse(id);
		return success ? "success" : "fail";
	}

}// class
