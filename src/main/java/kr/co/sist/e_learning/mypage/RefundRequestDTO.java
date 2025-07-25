package kr.co.sist.e_learning.mypage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefundRequestDTO {
    private String paymentId;
    private String refundReason;
}
