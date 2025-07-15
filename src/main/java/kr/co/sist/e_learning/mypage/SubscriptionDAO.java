package kr.co.sist.e_learning.mypage;

import java.util.List;
import java.util.Map;

public interface SubscriptionDAO {
    List<SubscriptionDTO> selectSubscriptions(long userSeq);
    void deleteSubscription(Map<String, Object> map);
}