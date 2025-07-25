package kr.co.sist.e_learning.admin.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class RefundDetailDTO {
    private Timestamp requestedAt;
    private Double refundAmount;
    private String status;
    private String reason;
    private String nickname;
}
