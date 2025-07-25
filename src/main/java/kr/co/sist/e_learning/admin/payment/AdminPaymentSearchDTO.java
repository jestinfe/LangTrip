package kr.co.sist.e_learning.admin.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminPaymentSearchDTO {
    private String paymentType; // payments or settlements
    private String searchKeyword;
    private String startDate;
    private String endDate;
    private String status; // For filtering by status
}
