package kr.co.sist.e_learning.admin.payment;

import kr.co.sist.e_learning.pagination.MyPageRequestDTO;
import kr.co.sist.e_learning.pagination.MyPageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(AdminPaymentServiceImpl.class);

    @Autowired
    private AdminPaymentMapper adminPaymentMapper;

    @Override
    public MyPageResponseDTO<PaymentDTO> getAllPayments(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO) {
        logger.info("getAllPayments called with searchDTO: {}, pageRequestDTO: {}", searchDTO, pageRequestDTO);
        Map<String, Object> params = new HashMap<>();
        params.put("searchDTO", searchDTO);
        params.put("pageRequestDTO", pageRequestDTO);

        int totalCount = adminPaymentMapper.countAllPayments(params);
        logger.info("Total payments count: {}", totalCount);
        List<PaymentDTO> dtoList = adminPaymentMapper.selectAllPayments(params);
        logger.info("Payments list size: {}", dtoList.size());

        return new MyPageResponseDTO<>(totalCount, pageRequestDTO, dtoList);
    }

    @Override
    public MyPageResponseDTO<SettlementDTO> getAllSettlements(AdminPaymentSearchDTO searchDTO, MyPageRequestDTO pageRequestDTO) {
        logger.info("getAllSettlements called with searchDTO: {}, pageRequestDTO: {}", searchDTO, pageRequestDTO);
        Map<String, Object> params = new HashMap<>();
        params.put("searchDTO", searchDTO);
        params.put("pageRequestDTO", pageRequestDTO);

        int totalCount = adminPaymentMapper.countAllSettlements(params);
        logger.info("Total settlements count: {}", totalCount);
        List<SettlementDTO> dtoList = adminPaymentMapper.selectAllSettlements(params);
        logger.info("Settlements list size: {}", dtoList.size());

        return new MyPageResponseDTO<>(totalCount, pageRequestDTO, dtoList);
    }

    @Override
    public PaymentDTO getPaymentDetail(String paymentSeq) {
        logger.info("getPaymentDetail called with paymentSeq: {}", paymentSeq);
        return adminPaymentMapper.selectPaymentDetail(paymentSeq);
    }

    @Override
    public SettlementDTO getSettlementDetail(String requestSeq) {
        logger.info("getSettlementDetail called with requestSeq: {}", requestSeq);
        return adminPaymentMapper.selectSettlementDetail(requestSeq);
    }

    @Override
    public void updatePaymentStatus(String paymentSeq, String status) {
        logger.info("updatePaymentStatus called with paymentSeq: {}, status: {}", paymentSeq, status);
        adminPaymentMapper.updatePaymentStatus(paymentSeq, status);
    }

    @Override
    public void updateSettlementStatus(String requestSeq, String status, String reason) {
        logger.info("updateSettlementStatus called with requestSeq: {}, status: {}, reason: {}", requestSeq, status, reason);
        adminPaymentMapper.updateSettlementStatus(requestSeq, status, reason);
        if ("settlement_completed".equals(status)) {
            SettlementDTO settlement = adminPaymentMapper.selectSettlementDetail(requestSeq);
            if (settlement != null && settlement.getWalletSeq() != null) {
                logger.info("Updating wallet for settlement completion. WalletSeq: {}", settlement.getWalletSeq());
                adminPaymentMapper.updatWalletBySettlement(settlement.getWalletSeq());
            }
        }
    }

    @Override
    public void updateRefundStatus(String paymentSeq, String status, String reason) {
        logger.info("updateRefundStatus called with paymentSeq: {}, status: {}, reason: {}", paymentSeq, status, reason);
        adminPaymentMapper.updateRefundStatus(paymentSeq, status, reason);
        adminPaymentMapper.updatePaymentStatus(paymentSeq, status); // Update payment status in payments table
        if ("refund_completed".equals(status)) {
            // 환불 승인 시 지갑에서 금액 차감
            PaymentDTO payment = adminPaymentMapper.selectPaymentDetail(paymentSeq);
            if (payment != null && payment.getWalletSeq() != null && payment.getPaymentAmount() != null) {
                logger.info("Updating wallet for refund completion. WalletSeq: {}, Amount: {}", payment.getWalletSeq(), payment.getPaymentAmount());
                adminPaymentMapper.updatWalletByRefund(payment.getWalletSeq(), payment.getPaymentAmount());
            }
        }
    }

    @Override
    public void updateWalletByRefund(String walletSeq, Double amount) {
        logger.info("updateWalletByRefund called with walletSeq: {}, amount: {}", walletSeq, amount);
        adminPaymentMapper.updatWalletByRefund(walletSeq, amount);
    }

    @Override
    public void updateWalletBySettlement(String walletSeq) {
        logger.info("updateWalletBySettlement called with walletSeq: {}", walletSeq);
        adminPaymentMapper.updatWalletBySettlement(walletSeq);
    }
}