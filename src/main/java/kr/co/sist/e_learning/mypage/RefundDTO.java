package kr.co.sist.e_learning.mypage;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundDTO {
    private String refundSeq;
    private String paymentSeq;
    private String accountSeq;
    private Long walletSeq;
    private Double refundAmount;
    private Timestamp requestedAt;
    private Timestamp processedAt;
    private String status;
    private String reason;
    private String rejectReason;
    private String paymentName; // 결제 상품명 추가
}
