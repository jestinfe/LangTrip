package kr.co.sist.e_learning.mypage;

import org.apache.ibatis.annotations.Param;

public interface MyPageDAO {
    MyPageDTO getUserInfo(long userSeq);  // 내정보 요약
    String selectProfilePath(long userSeq);  // 현재 경로 조회
    String getUserNickname(long userSeq);
    void updateProfile(@Param("userSeq") long userSeq, @Param("profile") String profile);
    String getUserPassword(long userSeq);
    int updateWithdrawalStatus(long userSeq, int reasonCode);

}
