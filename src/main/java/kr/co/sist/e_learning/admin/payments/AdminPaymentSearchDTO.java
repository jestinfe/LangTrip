package kr.co.sist.e_learning.admin.payments;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentSearchDTO {
    private String viewType;
    private String paymentType;
    private String searchKeyword;
    private String startDate;
    private String endDate;
    private int pageSize;
    private int page;
    private int startRow;
}
