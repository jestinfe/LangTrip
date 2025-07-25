package kr.co.sist.e_learning.admin.payment;

import kr.co.sist.e_learning.pagination.MyPageRequestDTO;
import kr.co.sist.e_learning.pagination.MyPageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminPaymentServiceImpl implements AdminPaymentService {


    @Autowired
    private AdminPaymentMapper adminPaymentMapper;

    @Override
    public MyPageResponseDTO<PaymentDTO> getAllPayments(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchDTO", searchDTO);
        params.put("pageRequestDTO", pageRequestDTO);

        int totalCount = adminPaymentMapper.countAllPayments(params);
        List<PaymentDTO> dtoList = adminPaymentMapper.selectAllPayments(params);

        return new MyPageResponseDTO<>(totalCount, pageRequestDTO, dtoList);
    }

    @Override
    public MyPageResponseDTO<SettlementDTO> getAllSettlements(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchDTO", searchDTO);
        params.put("pageRequestDTO", pageRequestDTO);

        int totalCount = adminPaymentMapper.countAllSettlements(params);
        List<SettlementDTO> dtoList = adminPaymentMapper.selectAllSettlements(params);

        return new MyPageResponseDTO<>(totalCount, pageRequestDTO, dtoList);
    }

    @Override
    public PaymentDTO getPaymentDetail(String paymentSeq) {
        return adminPaymentMapper.selectPaymentDetail(paymentSeq);
    }

    @Override
    public SettlementDTO getSettlementDetail(String requestSeq) {
        return adminPaymentMapper.selectSettlementDetail(requestSeq);
    }

    @Override
    public void updatePaymentStatus(String paymentSeq, String status) {
        adminPaymentMapper.updatePaymentStatus(paymentSeq, status);
    }

    @Override
    public void updateSettlementStatus(String requestSeq, String status, String reason) {
        adminPaymentMapper.updateSettlementStatus(requestSeq, status, reason);
        if ("settlement_completed".equals(status)) {
            SettlementDTO settlement = adminPaymentMapper.selectSettlementDetail(requestSeq);
            if (settlement != null && settlement.getWalletSeq() != null) {
                adminPaymentMapper.updatWalletBySettlement(settlement.getWalletSeq());
            }
        }
    }

    @Override
    public void updateRefundStatus(String paymentSeq, String status, String reason) {
        adminPaymentMapper.updateRefundStatus(paymentSeq, status, reason);
        adminPaymentMapper.updatePaymentStatus(paymentSeq, status); // Update payment status in payments table
        if ("refund_completed".equals(status)) {
            // 환불 승인 시 지갑에서 금액 차감
            PaymentDTO payment = adminPaymentMapper.selectPaymentDetail(paymentSeq);
            if (payment != null && payment.getWalletSeq() != null && payment.getPaymentAmount() != null) {
                adminPaymentMapper.updatWalletByRefund(payment.getWalletSeq(), payment.getPaymentAmount());
            }
        }
    }

    @Override
    public void updateWalletByRefund(String walletSeq, Double amount) {
        adminPaymentMapper.updatWalletByRefund(walletSeq, amount);
    }

    @Override
    public void updateWalletBySettlement(String walletSeq) {
        adminPaymentMapper.updatWalletBySettlement(walletSeq);
    }
}