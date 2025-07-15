package kr.co.sist.e_learning.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {
    private String followeeId;        // 강사 ID
    private String nickname;           // 강사 닉네임
    private String profile;            // 강사 프로필 이미지 경로
    private String subscribedAt;      // 구독일자
    private String title;              // 최신 영상 제목
    private String thumbnailName;     // 썸네일 제목
    private String thumbnailPath;      // 썸네일 경로
}
