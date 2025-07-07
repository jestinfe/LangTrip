package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDAOImpl implements SubscriptionDAO {

    @Autowired
    private SqlSessionTemplate sst;

    @Override
    public List<SubscriptionDTO> selectSubscriptions(String userSeq) {
        return sst.selectList("kr.co.sist.e_learning.mypage.SubscriptionMapper.selectSubscriptions", userSeq);
    }

    @Override
    public int deleteSubscription(String followerId, String followeeId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userSeq", followerId);         // 수정된 이름
        paramMap.put("instructorId", followeeId);    // 그대로 유지
        return sst.delete("kr.co.sist.e_learning.mypage.SubscriptionMapper.deleteSubscription", paramMap);
    }
}
