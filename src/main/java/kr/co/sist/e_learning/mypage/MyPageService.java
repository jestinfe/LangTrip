package kr.co.sist.e_learning.mypage;

import java.util.List;


public interface MyPageService {
    MyPageDTO getMyPageData(long userSeq);
    MyPageDTO getUserInfo(long userSeq);
    String selectProfilePath(long userSeq);
    void updateProfilePath(long userSeq, String newPath);
    List<LectureHistoryDTO> getLectureHistory(long userSeq);
    List<SubscriptionDTO> getSubscriptions(long userSeq);
    int cancelSubscription(long followerId, String followeeId);
}

