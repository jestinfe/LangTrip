package kr.co.sist.e_learning.admin.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class PaymentDTO {
    private String paymentSeq;
    private Long userSeq;
    private String nickname;
    private Double paymentAmount;
    private String paymentStatus;
    private Timestamp createdAt; // 결제일
    private Timestamp processedAt; // 결제 처리일 (기존 필드 유지)
    private String reason; // 환불 요청 사유
    private String walletSeq;
    private Timestamp refundRequestedAt; // 환불 요청일
    private Timestamp refundProcessedAt; // 환불 처리일
}