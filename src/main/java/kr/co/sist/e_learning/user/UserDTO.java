package kr.co.sist.e_learning.user;


import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long userSeq;
    private String email;
    private String password;
    private String nickname;
    private String status;
    private String profile;
    private String socialId;
    private String socialProvider;
    
    // 회원 관리. 정제균.
    private Date createdAt;
    private Date adminActedAt; // 관리자 조치일. 없으면 조치 없음으로.
    private int openedCourseCnt;
    private int activeCourseCnt;
    private String adminCheckedReason; // DB의 체크박스값을 문자열로 받음
    private List<String> adminCheckedReasonList; // 신고 사유 리스트
    private String adminCustomReasonTxt;
    private String title;
	private List<String> courseTitles;
	private List<String> openedCourses;
	private Long reportId;
	private boolean reportedTarget;
}
