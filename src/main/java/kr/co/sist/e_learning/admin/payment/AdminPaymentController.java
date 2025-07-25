package kr.co.sist.e_learning.admin.payment;

import kr.co.sist.e_learning.pagination.MyPageRequestDTO;
import kr.co.sist.e_learning.pagination.MyPageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin/payment")
public class AdminPaymentController {

    private static final Logger logger = LoggerFactory.getLogger(AdminPaymentController.class);

    @Autowired
    private AdminPaymentService adminPaymentService;

    /**
     * 전체 페이지 최초 로딩
     */
    @GetMapping
    public String adminPaymentPage(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO, Model model) {
        logger.info("adminPaymentPage called with searchDTO: {}, pageRequestDTO: {}", searchDTO, pageRequestDTO);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "admin/payment/admin_payment";
    }

    /**
     * 비동기 리스트 요청 (페이징 포함)
     */
    @GetMapping("/payments")
    public String getPayments(AdminPaymentSearchDTO searchDTO,
                              MyPageRequestDTO pageRequestDTO,
                              @RequestParam(name = "async", required = false, defaultValue = "false") boolean async,
                              Model model) {
        logger.info("getPayments called with searchDTO: {}, pageRequestDTO: {}, async: {}", searchDTO, pageRequestDTO, async);
        MyPageResponseDTO<PaymentDTO> pageResponse = adminPaymentService.getAllPayments(searchDTO, pageRequestDTO);

        model.addAttribute("list", pageResponse.getDtoList());
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("totalCount", pageResponse.getTotalCount());
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        if (async) {
            return "admin/payment/payment_list_fragment :: paymentList";
        }
        return "admin/payment/admin_payment";
    }

    /**
     * 정산 리스트
     */
    @GetMapping("/settlements")
    public String getSettlements(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO, Model model) {
        logger.info("getSettlements called with searchDTO: {}, pageRequestDTO: {}", searchDTO, pageRequestDTO);
        MyPageResponseDTO<SettlementDTO> pageResponse = adminPaymentService.getAllSettlements(searchDTO, pageRequestDTO);
        model.addAttribute("list", pageResponse.getDtoList());
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("totalCount", pageResponse.getTotalCount());
        return "admin/payment/settlement_list_fragment :: settlementList";
    }

    /**
     * 결제 상태 변경
     */
    @PostMapping("/update-status")
    @ResponseBody
    public String updatePaymentStatus(@RequestParam String paymentSeq, @RequestParam String status) {
        logger.info("updatePaymentStatus called with paymentSeq: {}, status: {}", paymentSeq, status);
        adminPaymentService.updatePaymentStatus(paymentSeq, status);
        return "SUCCESS";
    }

    /**
     * 정산 상태 변경
     */
    @PostMapping("/update-settlement-status")
    @ResponseBody
    public String updateSettlementStatus(@RequestParam String requestSeq, @RequestParam String status, @RequestParam(required = false) String reason) {
        logger.info("updateSettlementStatus called with requestSeq: {}, status: {}, reason: {}", requestSeq, status, reason);
        adminPaymentService.updateSettlementStatus(requestSeq, status, reason);
        return "SUCCESS";
    }

    /**
     * 환불 상태 변경
     */
    @PostMapping("/update-refund-status")
    @ResponseBody
    public String updateRefundStatus(@RequestParam String paymentSeq, @RequestParam String status, @RequestParam(required = false) String reason) {
        logger.info("updateRefundStatus called with paymentSeq: {}, status: {}, reason: {}", paymentSeq, status, reason);
        adminPaymentService.updateRefundStatus(paymentSeq, status, reason);
        adminPaymentService.updatePaymentStatus(paymentSeq, status);
        return "SUCCESS";
    }
}

