package kr.co.sist.e_learning.admin.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class SettlementDTO {
    private String requestSeq;
    private Long userSeq;
    private String nickname;
    private Double paidAmount;
    private String status;
    private Timestamp requestedAt;
    private Timestamp processedAt;
    private String rejectReason;
    private String walletSeq; // Added walletSeq
}
