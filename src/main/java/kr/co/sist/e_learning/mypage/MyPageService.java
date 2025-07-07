package kr.co.sist.e_learning.mypage;

import java.util.List;


public interface MyPageService {
    MyPageDTO getMyPageData(String userSeq);
    MyPageDTO getUserInfo(String userSeq);
    List<LectureHistoryDTO> getLectureHistory(String userSeq);
    List<SubscriptionDTO> getSubscriptions(String userSeq);
    int cancelSubscription(String followerId, String followeeId);
}

