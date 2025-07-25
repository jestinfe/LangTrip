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
public class PaymentsDTO {
    private String paymentSeq;
    private String impUid;
    private Long walletSeq;
    private String method;
    private String provider;
    private Double paymentAmount;
    private String paymentStatus;
    private Timestamp createdAt;
    private String currency;
    private String channel;
    private String name;
    private String receiptUrl;
    private String failReason;
    private String ipAddress;
    private String deviceInfo;
    private String userAgent;
    private Double feeRate;
    private Long userSeq;
    private Double mileAmount;
}
