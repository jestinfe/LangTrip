package kr.co.sist.e_learning.mypage;

import java.util.List;

public interface SubscriptionDAO {
    List<SubscriptionDTO> selectSubscriptions(String userSeq);
    int deleteSubscription(String followerId, String followeeId);
}
