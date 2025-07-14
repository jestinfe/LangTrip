package kr.co.sist.e_learning.mypage;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

public interface MyPageDAO {
    MyPageDTO getMyPageSummary(String userSeq);  // 대시보드 요약
    MyPageDTO selectUserInfo(String userSeq);    // 내 정보
    List<SubscriptionDTO> getSubscriptions(String userSeq);
    void unsubscribe(String userId, String instructorId);
}
