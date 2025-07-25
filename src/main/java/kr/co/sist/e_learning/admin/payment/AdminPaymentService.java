package kr.co.sist.e_learning.admin.payment;

import kr.co.sist.e_learning.pagination.MyPageRequestDTO;
import kr.co.sist.e_learning.pagination.MyPageResponseDTO;

public interface AdminPaymentService {
    MyPageResponseDTO<PaymentDTO> getAllPayments(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO);
    MyPageResponseDTO<SettlementDTO> getAllSettlements(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO);
    PaymentDTO getPaymentDetail(String paymentSeq);
    SettlementDTO getSettlementDetail(String requestSeq);
    void updatePaymentStatus(String paymentSeq, String status);
    void updateSettlementStatus(String requestSeq, String status, String reason);
    void updateRefundStatus(String paymentSeq, String status, String reason);
    void updateWalletByRefund(String walletSeq, Double amount);
    void updateWalletBySettlement(String walletSeq);
}