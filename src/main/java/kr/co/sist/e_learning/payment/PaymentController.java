package kr.co.sist.e_learning.payment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

	@GetMapping("user/payments")
	public String getMethodName() {
		return "user/payments/payments";
	}

	@GetMapping("/payment/success")
	public String paymentSuccess(@RequestParam("payment_amount") int paymentAmount,
			@RequestParam("mile_amount") int mileAmount,
			Model model) {

		model.addAttribute("paymentAmount", paymentAmount);
		model.addAttribute("mileAmount", mileAmount);

		return "user/payments/payment_success";
	}
}
