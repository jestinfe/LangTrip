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
public class SettlementRequestDTO {
    private Long requestSeq;
    private Long userSeq;
    private Long walletSeq;
    private Double totalMile;
    private String status;
    private Timestamp requestedAt;
    private Timestamp processedAt;
    private Double companyFee;
    private Double paidAmount;
    private String rejectReason; // 반려 사유 추가
}
