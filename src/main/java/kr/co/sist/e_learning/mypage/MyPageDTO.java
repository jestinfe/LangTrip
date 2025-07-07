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
public class MyPageDTO {

	//회원정보
    private String userSeq;
    private String email;
    private String password;
    private String nickname;
    private String status;
    private String profile;
    private String socialId;
    private String socialProvider;
    
    //통계자료
    private int completedCourses;
    private int totalStudyTime;
    private double quizAccuracy;
    private String donationLevel;
    private int totalDonation;
    
}
