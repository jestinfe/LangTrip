package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyPageDAOImpl implements MyPageDAO {
    @Autowired
    private SqlSession session;

    @Autowired
    private SqlSessionTemplate sst;
    
    @Override
    public MyPageDTO getMyPageSummary(String userId) {
        return session.selectOne("kr.co.sist.e_learning.mypage.MyPageMapper.selectMyPageSummary", userId);
    }

    @Override
    public MyPageDTO selectUserInfo(String userSeq) {
        return sst.selectOne("kr.co.sist.e_learning.mypage.MyPageMapper.selectUserInfo", userSeq);
    }
    
    @Override
    public List<SubscriptionDTO> getSubscriptions(String userId) {
        return session.selectList("kr.co.sist.e_learning.mypage.MyPageMapper.selectSubscriptions", userId);
    }

    @Override
    public void unsubscribe(String userId, String instructorId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("instructorId", instructorId);
        session.delete("kr.co.sist.e_learning.mypage.MyPageMapper.deleteSubscription", map);
    }
}