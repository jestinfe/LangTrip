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
    
    private final String NAMESPACE = "kr.co.sist.e_learning.mypage.MyPageMapper";
    
    @Override
    public MyPageDTO getMyPageSummary(long userSeq) {
        return session.selectOne(NAMESPACE + ".selectMyPageSummary", userSeq);
    }

    @Override
    public MyPageDTO selectUserInfo(long userSeq) {
        return sst.selectOne(NAMESPACE + ".selectUserInfo", userSeq);
    }
    
    
    @Override
    public String selectProfilePath(long userSeq) {
        return session.selectOne(NAMESPACE + ".selectProfilePath", userSeq);
    }
    
    @Override
    public void updateProfile(long userSeq, String profile) {
        Map<String, Object> map = new HashMap<>();
        map.put("userSeq", userSeq);
        map.put("profile", profile);
        session.update(NAMESPACE + ".updateProfile", map);
    }

    
    @Override
    public List<SubscriptionDTO> getSubscriptions(long userSeq) {
        return session.selectList(NAMESPACE + ".selectSubscriptions", userSeq);
    }

    @Override
    public void unsubscribe(long userSeq, String instructorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userSeq", userSeq);
        map.put("instructorId", instructorId);
        session.delete(NAMESPACE + ".deleteSubscription", map);
    }
}