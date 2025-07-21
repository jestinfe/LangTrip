package kr.co.sist.e_learning.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 결제 관련 컨트롤러
 */
@RestController
@RequestMapping("/payment")
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 결제 처리
     */
    @PostMapping
    public ResponseEntity<?> savePayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userSeq;
            if (principal instanceof Long) {
                userSeq = (Long) principal;
            } else {
                throw new IllegalStateException("User principal is not of expected type Long");
            }
            paymentRequestDTO.setUserSeq(userSeq);
            paymentService.savePayment(paymentRequestDTO);

            String redirectUrl = String.format("/payment/success?payment_amount=%d&mile_amount=%d",
                    paymentRequestDTO.getPaymentAmount(), paymentRequestDTO.getMileAmount());

            return ResponseEntity.ok().body(new PaymentSuccessResponseDTO(true, "결제가 정상 처리되었습니다.", redirectUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(new PaymentSimpleResponseDTO(false, "결제 처리 중 오류가 발생했습니다."));
        }
    }

    /**
     * 결제 중간 취소 (사용자 결제 창에서 취소 눌렀을 때 등)
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userSeq;
            if (principal instanceof Long) {
                userSeq = (Long) principal;
            } else {
                throw new IllegalStateException("User principal is not of expected type Long");
            }
            paymentRequestDTO.setUserSeq(userSeq);
            paymentService.cancelPayment(paymentRequestDTO);
            return ResponseEntity.ok().body(new PaymentSimpleResponseDTO(true, "결제가 취소되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(new PaymentSimpleResponseDTO(false, "결제 취소 중 오류가 발생했습니다."));
        }
    }
}

