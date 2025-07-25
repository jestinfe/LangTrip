package kr.co.sist.e_learning.payment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;





@Controller
public class PaymentController {

	@GetMapping("user/payments")
	public String getMethodName() {
		return "user/payments/payments";
	}

	@GetMapping("/payment/success")
	public String paymentSuccessPage(
			@RequestParam("lectureNo") String lectureNo,
			Model model,
			HttpSession session) {

		// 세션에서 결제 금액과 마일 정보 가져오기
		
	
		String storedLectureNo = (String) session.getAttribute("lectureNoForSuccessPage");
		

		// 모델에 추가
		
		model.addAttribute("lectureNo", storedLectureNo != null ? storedLectureNo : lectureNo);

		return "user/payments/payment_success";
	}
}