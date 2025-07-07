package kr.co.sist.e_learning.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {

    private String instructorId;
    private String instructorName;
    private int courseCount;
    private String subscribedAt;
}
