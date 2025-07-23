package kr.co.sist.e_learning.admin.payments;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentListDTO {
    private String paymentId;
    private String userId;
    private String lectureTitle;
    private Long amount;
    private String method;
    private String status;
    private LocalDateTime paymentDate;
}
