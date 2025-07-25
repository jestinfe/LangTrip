package kr.co.sist.e_learning.util;

import org.springframework.stereotype.Component;

@Component("paymentStatusConverter")
public class PaymentStatusConverter {

    public String convert(String status) {
    	 if (status == null) {
    	        return "알 수 없음"; // 또는 빈 문자열, 혹은 throw 예외
    	    }
        switch (status) {
            case "payment_success":
                return "결제 성공";
            case "payment_canceled":
                return "결제 취소";
            case "refund_requested":
                return "환불 신청";
            case "refund_completed":
                return "환불 성공";
            case "refund_rejected":
                return "환불 반려";
            default:
                return status;
        }
    }
}
