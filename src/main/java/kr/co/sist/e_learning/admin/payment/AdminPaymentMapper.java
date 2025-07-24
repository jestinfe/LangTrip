package kr.co.sist.e_learning.admin.payment;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminPaymentMapper {
    List<PaymentDTO> selectAllPayments(Map<String, Object> params);
    int countAllPayments(Map<String, Object> params);
    List<SettlementDTO> selectAllSettlements(Map<String, Object> params);
    int countAllSettlements(Map<String, Object> params);
    PaymentDTO selectPaymentDetail(String paymentSeq);
    SettlementDTO selectSettlementDetail(String requestSeq);
    void updatePaymentStatus(@Param("paymentSeq") String paymentSeq, @Param("status") String status);
    void updateSettlementStatus(@Param("requestSeq") String requestSeq, @Param("status") String status, @Param("reason") String reason);
    void updateRefundStatus(@Param("paymentSeq") String paymentSeq, @Param("status") String status, @Param("reason") String reason);
    void updatWalletByRefund(@Param("walletSeq") String walletSeq, @Param("amount") Double amount);
    void updatWalletBySettlement(@Param("walletSeq") String walletSeq);
}