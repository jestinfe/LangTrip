package kr.co.sist.e_learning.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyPageRequestDTO {
    private int page = 1; // 요청 페이지 번호
    private int size = 10; // 한 페이지당 개수
    private String orderBy = "logTime"; // 정렬 기준 필드 (기본값 설정)
    private String sort = "desc"; // 정렬 방식 (asc, desc) (기본값 설정)

    // AdminPaymentSearchDTO 관련 필드 (검색 조건)
    private String paymentType;
    private String searchKeyword;
    private String startDate;
    private String endDate;
    private String status;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    public String getLink(int pageNum) {
        StringBuilder builder = new StringBuilder();
        builder.append("?page=" + pageNum);
        builder.append("&size=" + this.size);

        if (this.orderBy != null && !this.orderBy.isEmpty()) {
            builder.append("&orderBy=" + this.orderBy);
        }
        if (this.sort != null && !this.sort.isEmpty()) {
            builder.append("&sort=" + this.sort);
        }

        if (this.paymentType != null && !this.paymentType.isEmpty()) {
            builder.append("&paymentType=" + this.paymentType);
        }
        if (this.searchKeyword != null && !this.searchKeyword.isEmpty()) {
            builder.append("&searchKeyword=" + URLEncoder.encode(this.searchKeyword, StandardCharsets.UTF_8));
        }
        if (this.startDate != null && !this.startDate.isEmpty()) {
            builder.append("&startDate=" + this.startDate);
        }
        if (this.endDate != null && !this.endDate.isEmpty()) {
            builder.append("&endDate=" + this.endDate);
        }
        if (this.status != null && !this.status.isEmpty()) {
            builder.append("&status=" + this.status);
        }

        return builder.toString();
    }
}