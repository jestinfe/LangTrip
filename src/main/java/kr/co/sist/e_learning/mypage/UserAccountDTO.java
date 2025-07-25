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
public class UserAccountDTO {
    private String accountSeq;
    private Long userSeq;
    private String bankCode;
    private String accountNum;
    private String holderName;
    private Integer verified;
    private Timestamp createdAt;
}
