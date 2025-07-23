package kr.co.sist.e_learning.admin.dash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashDTO {
	private String hour; 			  // 시간 필드 추가 (시간별 접속 통계)
	private int accessCount; 		  // 해당 시간에 접속한 수
	
    private String regDate;           // 일별 날짜
    private int dailyCount;           // 일별 가입자 수 카운트
    
    private String signupPath;        // 유입 경로
    private String unsignPath;        // 탈퇴 사유
    private int unsignCount;          // 탈퇴자 수 카운트
    
    private String category;          // 카테고리 (영어, 일본어, 중국어 등)
    private int courseCount;          // 카테고리별 강의 등록 수

    private String difficulty;  	  // 강의 난이도
    private int difficultyCount; 	  // 난이도별 강의 수 카운트
    
    private Long bannerId;            // 광고 배너 ID
    private String title;             // 광고 제목
    private int clickCount;           // 광고 클릭 수
}
