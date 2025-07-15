package kr.co.sist.e_learning.mypage;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageDAO {
    MyPageDTO getMyPageSummary(long userSeq);  // 대시보드 요약
    MyPageDTO selectUserInfo(long userSeq);    // 내 정보
    String selectProfilePath(long userSeq);  // 현재 경로 조회
    void updateProfile(@Param("userSeq") long userSeq, @Param("profile") String profile);
    List<SubscriptionDTO> getSubscriptions(long userSeq);
    void unsubscribe(long userSeq, String instructorId);
}
