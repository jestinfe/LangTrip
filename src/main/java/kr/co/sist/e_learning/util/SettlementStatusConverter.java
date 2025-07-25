package kr.co.sist.e_learning.util;

import org.springframework.stereotype.Component;

@Component("settlementStatusConverter")
public class SettlementStatusConverter {

    public String convert(String status) {
        switch (status) {
            case "settlement_requested":
                return "정산 신청";
            case "settlement_completed":
                return "정산 성공";
            case "settlement_failed":
                return "정산 실패";
            default:
                return status;
        }
    }
}
