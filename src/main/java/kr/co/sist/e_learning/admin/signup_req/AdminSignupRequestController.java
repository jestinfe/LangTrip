package kr.co.sist.e_learning.admin.signup_req;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/signup/requests")
public class AdminSignupRequestController {

    @Autowired
    private AdminSignupRequestService signupRequestService;

    // 가입 요청 목록 조회
    @GetMapping
    public String showRequestList(Model model,
                                  @ModelAttribute("message") String message) {
        List<AdminSignupRequestDTO> list = signupRequestService.listPendingRequests();
        model.addAttribute("signupList", list);
        model.addAttribute("message", message); // Flash 메시지 표시용
        return "admin/account/signup_requests"; // ← HTML 파일명에 맞게 유지
    }

    // 가입 요청 상세 조회
    @GetMapping("/{id}")
    public String showRequestDetail(@PathVariable("id") String requestId, Model model) {
        AdminSignupRequestDTO dto = signupRequestService.getRequestById(requestId);
        model.addAttribute("signup", dto);
        return "admin/account/signup_detail";
    }

    // 승인 처리
    @PostMapping("/{id}/approve")
    public String approveRequest(@PathVariable("id") String requestId,
                                 RedirectAttributes redirectAttributes) {
        signupRequestService.approveRequest(requestId);
        redirectAttributes.addFlashAttribute("message", "가입 요청이 승인되었습니다.");
        return "redirect:/admin/account/signup_requests";
    }

    // 미승인 처리
    @PostMapping("/{id}/reject")
    public String rejectRequest(@PathVariable("id") String requestId,
                                RedirectAttributes redirectAttributes) {
        signupRequestService.rejectRequest(requestId);
        redirectAttributes.addFlashAttribute("message", "가입 요청이 거절되었습니다.");
        return "redirect:/admin/account/requests";
    }
}
